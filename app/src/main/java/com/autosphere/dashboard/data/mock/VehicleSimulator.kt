package com.autosphere.dashboard.data.mock

import com.autosphere.dashboard.domain.model.DrivingMode
import com.autosphere.dashboard.domain.model.Gear
import com.autosphere.dashboard.domain.model.TirePressure
import com.autosphere.dashboard.domain.model.VehicleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * Realistic Electric Vehicle Simulator.
 * Simulates motor telemetry, battery drain, regenerative braking,
 * dynamic cruising speed, gear logic, tire pressure, and charging.
 */
class VehicleSimulator(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private val _vehicleState = MutableStateFlow(
        VehicleState(
            speedKmh = 68.0f,
            rpm = 5200.0f,
            gear = Gear.DRIVE,
            batteryPercent = 82,
            estimatedRangeKm = 405,
            odometerKm = 14820.4f,
            outsideTempC = 21.5f,
            tirePressure = TirePressure(35.0f, 35.1f, 34.9f, 35.0f),
            drivingMode = DrivingMode.COMFORT,
            isHeadlightsOn = true,
            isDoorsLocked = true,
            isParkingBrakeEngaged = false,
            isCharging = false,
            chargingPowerKw = 0f,
            energyConsumptionWhPerKm = 168,
            motorTempC = 64.0f,
            batteryVoltage = 398.2f,
            isConnectedToCarService = false,
            isDemoMode = true
        )
    )
    val vehicleState: StateFlow<VehicleState> = _vehicleState.asStateFlow()

    private var simulationJob: Job? = null
    private var targetSpeedKmh: Float = 72.0f
    private var isAccelerating = true

    init {
        startSimulation()
    }

    fun startSimulation() {
        if (simulationJob?.isActive == true) return

        simulationJob = scope.launch {
            while (isActive) {
                delay(600) // ~1.6 Hz update rate for smooth dashboard telemetry
                updateTelemetryCycle()
            }
        }
    }

    fun stopSimulation() {
        simulationJob?.cancel()
        simulationJob = null
    }

    private fun updateTelemetryCycle() {
        val current = _vehicleState.value

        if (current.isCharging) {
            // Charging simulation: battery fills up, speed = 0, gear = P
            val newBattery = min(100, current.batteryPercent + if (Random.nextInt(5) == 0) 1 else 0)
            val newRange = calculateRange(newBattery, current.drivingMode)
            _vehicleState.value = current.copy(
                speedKmh = 0f,
                rpm = 0f,
                gear = Gear.PARK,
                isParkingBrakeEngaged = true,
                batteryPercent = newBattery,
                estimatedRangeKm = newRange,
                chargingPowerKw = 148.5f + Random.nextFloat() * 4.0f,
                energyConsumptionWhPerKm = 0
            )
            return
        }

        // When in PARK or NEUTRAL, vehicle cannot move
        if (current.gear == Gear.PARK || current.gear == Gear.NEUTRAL) {
            _vehicleState.value = current.copy(
                speedKmh = 0f,
                rpm = 0f,
                energyConsumptionWhPerKm = 12
            )
            return
        }

        // Dynamic speed variation around target speed
        if (Random.nextInt(15) == 0) {
            // Change target cruising speed periodically (between 45 and 115 km/h)
            targetSpeedKmh = when (current.drivingMode) {
                DrivingMode.ECO -> 55.0f + Random.nextFloat() * 30.0f
                DrivingMode.COMFORT -> 65.0f + Random.nextFloat() * 40.0f
                DrivingMode.SPORT -> 80.0f + Random.nextFloat() * 45.0f
            }
            isAccelerating = targetSpeedKmh > current.speedKmh
        }

        val speedDelta = if (isAccelerating) {
            when (current.drivingMode) {
                DrivingMode.SPORT -> 2.2f + Random.nextFloat() * 1.5f
                DrivingMode.COMFORT -> 1.4f + Random.nextFloat() * 1.0f
                DrivingMode.ECO -> 0.8f + Random.nextFloat() * 0.8f
            }
        } else {
            -1.5f - Random.nextFloat() * 1.0f
        }

        val newSpeed = (current.speedKmh + speedDelta).coerceIn(35.0f, 130.0f)
        if (newSpeed >= targetSpeedKmh) isAccelerating = false
        if (newSpeed <= 40.0f) isAccelerating = true

        // RPM is proportional to EV motor speed (~76.5 rpm per km/h)
        val newRpm = newSpeed * 76.5f

        // Distance traveled in this time step (600ms = 0.000166 hours)
        val distanceTraveledKm = (newSpeed * (0.6f / 3600.0f))
        val newOdometer = current.odometerKm + distanceTraveledKm

        // Wh/km consumption calculation
        val baseConsumption = when (current.drivingMode) {
            DrivingMode.ECO -> 145
            DrivingMode.COMFORT -> 172
            DrivingMode.SPORT -> 215
        }
        val dynamicConsumption = (baseConsumption + (newSpeed - 60f) * 1.2f).toInt().coerceAtLeast(110)

        // Subtle battery discharge over distance
        var newBattery = current.batteryPercent
        if (Random.nextInt(80) == 0 && newBattery > 5) {
            newBattery -= 1
        }

        val newRange = calculateRange(newBattery, current.drivingMode)

        // Micro-fluctuations for realistic sensors
        val outsideTempJitter = (Random.nextFloat() - 0.5f) * 0.04f
        val newOutsideTemp = (current.outsideTempC + outsideTempJitter).coerceIn(18.0f, 26.0f)

        val tireJitter = (Random.nextFloat() - 0.5f) * 0.02f
        val newTires = TirePressure(
            frontLeftPsi = (current.tirePressure.frontLeftPsi + tireJitter).coerceIn(34.2f, 35.8f),
            frontRightPsi = (current.tirePressure.frontRightRightOrJitter(tireJitter)).coerceIn(34.2f, 35.8f),
            rearLeftPsi = (current.tirePressure.rearLeftPsi + tireJitter).coerceIn(34.0f, 35.6f),
            rearRightPsi = (current.tirePressure.rearRightPsi + tireJitter).coerceIn(34.0f, 35.6f)
        )

        _vehicleState.value = current.copy(
            speedKmh = newSpeed,
            rpm = newRpm,
            odometerKm = newOdometer,
            batteryPercent = newBattery,
            estimatedRangeKm = newRange,
            outsideTempC = newOutsideTemp,
            tirePressure = newTires,
            energyConsumptionWhPerKm = dynamicConsumption,
            motorTempC = (58.0f + (newSpeed / 130.0f) * 18.0f),
            batteryVoltage = (370.0f + (newBattery / 100.0f) * 35.0f)
        )
    }

    private fun TirePressure.frontRightRightOrJitter(jitter: Float): Float {
        return frontRightPsi + jitter
    }

    private fun calculateRange(batteryPercent: Int, mode: DrivingMode): Int {
        val baseRange = batteryPercent * 4.9f
        val factor = when (mode) {
            DrivingMode.ECO -> 1.12f
            DrivingMode.COMFORT -> 1.0f
            DrivingMode.SPORT -> 0.88f
        }
        return (baseRange * factor).toInt()
    }

    fun setGear(gear: Gear) {
        val current = _vehicleState.value
        val speed = if (gear == Gear.PARK || gear == Gear.NEUTRAL) 0f else current.speedKmh
        val parkingBrake = gear == Gear.PARK
        _vehicleState.value = current.copy(
            gear = gear,
            speedKmh = speed,
            isParkingBrakeEngaged = parkingBrake
        )
    }

    fun toggleHeadlights() {
        val current = _vehicleState.value
        _vehicleState.value = current.copy(isHeadlightsOn = !current.isHeadlightsOn)
    }

    fun toggleDoorLocks() {
        val current = _vehicleState.value
        _vehicleState.value = current.copy(isDoorsLocked = !current.isDoorsLocked)
    }

    fun toggleParkingBrake() {
        val current = _vehicleState.value
        val newState = !current.isParkingBrakeEngaged
        val speed = if (newState) 0f else current.speedKmh
        _vehicleState.value = current.copy(
            isParkingBrakeEngaged = newState,
            speedKmh = speed
        )
    }

    fun setDrivingMode(mode: DrivingMode) {
        val current = _vehicleState.value
        val newRange = calculateRange(current.batteryPercent, mode)
        _vehicleState.value = current.copy(
            drivingMode = mode,
            estimatedRangeKm = newRange
        )
    }

    fun setSimulatedSpeed(speedKmh: Float) {
        targetSpeedKmh = speedKmh.coerceIn(0f, 220f)
        val current = _vehicleState.value
        _vehicleState.value = current.copy(
            speedKmh = targetSpeedKmh,
            rpm = targetSpeedKmh * 76.5f
        )
    }

    fun setCharging(isCharging: Boolean) {
        val current = _vehicleState.value
        _vehicleState.value = current.copy(
            isCharging = isCharging,
            chargingPowerKw = if (isCharging) 150.0f else 0f,
            gear = if (isCharging) Gear.PARK else current.gear,
            speedKmh = if (isCharging) 0f else current.speedKmh
        )
    }

    fun setDemoMode(enabled: Boolean) {
        val current = _vehicleState.value
        _vehicleState.value = current.copy(isDemoMode = enabled)
        if (enabled) {
            startSimulation()
        } else {
            stopSimulation()
        }
    }
}
