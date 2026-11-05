package com.autosphere.dashboard.data.repository

import com.autosphere.dashboard.data.mock.VehicleSimulator
import com.autosphere.dashboard.data.vehicle.CarPropertyManagerWrapper
import com.autosphere.dashboard.domain.model.DrivingMode
import com.autosphere.dashboard.domain.model.Gear
import com.autosphere.dashboard.domain.model.VehicleState
import com.autosphere.dashboard.domain.repository.VehicleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VehicleRepositoryImpl(
    private val simulator: VehicleSimulator,
    private val carPropertyWrapper: CarPropertyManagerWrapper,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : VehicleRepository {

    private val _isDemoMode = MutableStateFlow(true)

    init {
        scope.launch {
            carPropertyWrapper.connect()
        }
    }

    override val vehicleState: StateFlow<VehicleState> = combine(
        simulator.vehicleState,
        carPropertyWrapper.realVehicleState,
        carPropertyWrapper.isCarServiceConnected,
        _isDemoMode
    ) { simState, realState, isCarConnected, isDemo ->
        if (isDemo || realState == null || !isCarConnected) {
            simState.copy(
                isDemoMode = isDemo,
                isConnectedToCarService = isCarConnected
            )
        } else {
            // Real vehicle data from AAOS CarPropertyManager
            realState.copy(
                isDemoMode = false,
                isConnectedToCarService = true
            )
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = simulator.vehicleState.value
    )

    override fun setGear(gear: Gear) {
        simulator.setGear(gear)
    }

    override fun toggleHeadlights() {
        simulator.toggleHeadlights()
    }

    override fun toggleDoorLocks() {
        simulator.toggleDoorLocks()
    }

    override fun toggleParkingBrake() {
        simulator.toggleParkingBrake()
    }

    override fun setDrivingMode(mode: DrivingMode) {
        simulator.setDrivingMode(mode)
    }

    override fun setDemoMode(enabled: Boolean) {
        _isDemoMode.value = enabled
        simulator.setDemoMode(enabled)
    }

    override fun setSimulatedSpeed(speedKmh: Float) {
        simulator.setSimulatedSpeed(speedKmh)
    }

    override fun setCharging(isCharging: Boolean) {
        simulator.setCharging(isCharging)
    }
}
