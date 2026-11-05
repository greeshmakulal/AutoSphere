package com.autosphere.dashboard.data.vehicle

import android.car.Car
import android.car.VehicleAreaType
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.Context
import android.util.Log
import com.autosphere.dashboard.domain.model.Gear
import com.autosphere.dashboard.domain.model.TirePressure
import com.autosphere.dashboard.domain.model.VehicleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Android Automotive OS CarPropertyManager wrapper.
 * Safely accesses AAOS hardware sensors and vehicle telemetry,
 * with graceful error handling for missing permissions and unsupported properties.
 */
class CarPropertyManagerWrapper(
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    companion object {
        private const val TAG = "CarPropertyMgr"
    }

    private var car: Car? = null
    private var carPropertyManager: CarPropertyManager? = null

    private val _realVehicleState = MutableStateFlow<VehicleState?>(null)
    val realVehicleState: StateFlow<VehicleState?> = _realVehicleState.asStateFlow()

    private val _isCarServiceConnected = MutableStateFlow(false)
    val isCarServiceConnected: StateFlow<Boolean> = _isCarServiceConnected.asStateFlow()

    private val unavailableProperties = mutableSetOf<String>()

    private val propertyCallback = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(value: CarPropertyValue<*>) {
            handleCarPropertyValue(value)
        }

        override fun onErrorEvent(propId: Int, zone: Int) {
            Log.w(TAG, "CarProperty error for propId: $propId in zone: $zone")
        }
    }

    suspend fun connect(): Boolean = withContext(Dispatchers.IO) {
        try {
            car = Car.createCar(context.applicationContext)
            carPropertyManager = car?.getCarManager(Car.PROPERTY_SERVICE) as? CarPropertyManager

            val connected = carPropertyManager != null && car?.isConnected == true
            _isCarServiceConnected.value = connected

            if (connected) {
                registerPropertyListeners()
                readInitialProperties()
            }
            connected
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException connecting to AAOS Car Service: ${e.message}")
            _isCarServiceConnected.value = false
            false
        } catch (e: Exception) {
            Log.e(TAG, "Exception connecting to AAOS Car Service: ${e.message}")
            _isCarServiceConnected.value = false
            false
        }
    }

    private fun registerPropertyListeners() {
        val manager = carPropertyManager ?: return

        val rateNormal = CarPropertyManager.SENSOR_RATE_NORMAL

        safeRegisterProperty(manager, VehiclePropertyIds.PERF_VEHICLE_SPEED, rateNormal)
        safeRegisterProperty(manager, VehiclePropertyIds.GEAR_SELECTION, rateNormal)
        safeRegisterProperty(manager, VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE, rateNormal)
        safeRegisterProperty(manager, VehiclePropertyIds.PERF_ODOMETER, rateNormal)
        safeRegisterProperty(manager, VehiclePropertyIds.PARKING_BRAKE_ON, rateNormal)
    }

    private fun safeRegisterProperty(manager: CarPropertyManager, propertyId: Int, rate: Float) {
        try {
            manager.registerCallback(propertyCallback, propertyId, rate)
        } catch (e: SecurityException) {
            Log.w(TAG, "Permission denied for propertyId: $propertyId")
            unavailableProperties.add("prop_$propertyId")
        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "Property not supported: $propertyId")
            unavailableProperties.add("prop_$propertyId")
        } catch (e: Exception) {
            Log.w(TAG, "Could not register listener for property: $propertyId: ${e.message}")
        }
    }

    private fun readInitialProperties() {
        val manager = carPropertyManager ?: return
        var state = _realVehicleState.value ?: VehicleState(
            isConnectedToCarService = true,
            isDemoMode = false,
            unavailableProperties = unavailableProperties
        )

        try {
            // Speed (m/s -> km/h)
            if (isPropertyAvailable(manager, VehiclePropertyIds.PERF_VEHICLE_SPEED)) {
                val speedMs = manager.getFloatProperty(VehiclePropertyIds.PERF_VEHICLE_SPEED, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL)
                state = state.copy(speedKmh = speedMs * 3.6f)
            }
        } catch (e: Exception) {
            unavailableProperties.add("PERF_VEHICLE_SPEED")
        }

        try {
            // Gear
            if (isPropertyAvailable(manager, VehiclePropertyIds.GEAR_SELECTION)) {
                val gearInt = manager.getIntProperty(VehiclePropertyIds.GEAR_SELECTION, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL)
                state = state.copy(gear = mapIntToGear(gearInt))
            }
        } catch (e: Exception) {
            unavailableProperties.add("GEAR_SELECTION")
        }

        try {
            // Outside temperature
            if (isPropertyAvailable(manager, VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE)) {
                val tempC = manager.getFloatProperty(VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL)
                state = state.copy(outsideTempC = tempC)
            }
        } catch (e: Exception) {
            unavailableProperties.add("ENV_OUTSIDE_TEMPERATURE")
        }

        try {
            // Odometer
            if (isPropertyAvailable(manager, VehiclePropertyIds.PERF_ODOMETER)) {
                val odoKm = manager.getFloatProperty(VehiclePropertyIds.PERF_ODOMETER, VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL)
                state = state.copy(odometerKm = odoKm)
            }
        } catch (e: Exception) {
            unavailableProperties.add("PERF_ODOMETER")
        }

        _realVehicleState.value = state.copy(
            isConnectedToCarService = true,
            isDemoMode = false,
            unavailableProperties = unavailableProperties
        )
    }

    private fun isPropertyAvailable(manager: CarPropertyManager, propId: Int): Boolean {
        return try {
            manager.getCarPropertyConfig(propId) != null
        } catch (e: Exception) {
            false
        }
    }

    private fun handleCarPropertyValue(value: CarPropertyValue<*>) {
        val current = _realVehicleState.value ?: VehicleState(
            isConnectedToCarService = true,
            isDemoMode = false
        )

        when (value.propertyId) {
            VehiclePropertyIds.PERF_VEHICLE_SPEED -> {
                val speedMs = (value.value as? Float) ?: return
                val speedKmh = speedMs * 3.6f
                _realVehicleState.value = current.copy(
                    speedKmh = speedKmh,
                    rpm = speedKmh * 76.5f
                )
            }
            VehiclePropertyIds.GEAR_SELECTION -> {
                val gearInt = (value.value as? Int) ?: return
                _realVehicleState.value = current.copy(gear = mapIntToGear(gearInt))
            }
            VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE -> {
                val temp = (value.value as? Float) ?: return
                _realVehicleState.value = current.copy(outsideTempC = temp)
            }
            VehiclePropertyIds.PERF_ODOMETER -> {
                val odo = (value.value as? Float) ?: return
                _realVehicleState.value = current.copy(odometerKm = odo)
            }
            VehiclePropertyIds.PARKING_BRAKE_ON -> {
                val brake = (value.value as? Boolean) ?: return
                _realVehicleState.value = current.copy(isParkingBrakeEngaged = brake)
            }
        }
    }

    private fun mapIntToGear(gearVal: Int): Gear {
        return when (gearVal) {
            1 -> Gear.PARK
            2 -> Gear.REVERSE
            4 -> Gear.NEUTRAL
            8 -> Gear.DRIVE
            else -> Gear.PARK
        }
    }

    fun disconnect() {
        try {
            carPropertyManager?.unregisterCallback(propertyCallback)
        } catch (e: Exception) {
            Log.w(TAG, "Error unregistering callback: ${e.message}")
        }
        try {
            car?.disconnect()
        } catch (e: Exception) {
            Log.w(TAG, "Error disconnecting Car: ${e.message}")
        }
        carPropertyManager = null
        car = null
        _isCarServiceConnected.value = false
    }
}
