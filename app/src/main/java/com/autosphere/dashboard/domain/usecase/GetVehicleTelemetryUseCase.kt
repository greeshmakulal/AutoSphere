package com.autosphere.dashboard.domain.usecase

import com.autosphere.dashboard.domain.model.DrivingMode
import com.autosphere.dashboard.domain.model.Gear
import com.autosphere.dashboard.domain.model.VehicleState
import com.autosphere.dashboard.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.StateFlow

class GetVehicleTelemetryUseCase(
    private val repository: VehicleRepository
) {
    val telemetry: StateFlow<VehicleState> = repository.vehicleState

    fun setGear(gear: Gear) = repository.setGear(gear)
    fun toggleHeadlights() = repository.toggleHeadlights()
    fun toggleDoorLocks() = repository.toggleDoorLocks()
    fun toggleParkingBrake() = repository.toggleParkingBrake()
    fun setDrivingMode(mode: DrivingMode) = repository.setDrivingMode(mode)
    fun setDemoMode(enabled: Boolean) = repository.setDemoMode(enabled)
    fun setSimulatedSpeed(speedKmh: Float) = repository.setSimulatedSpeed(speedKmh)
    fun setCharging(isCharging: Boolean) = repository.setCharging(isCharging)
}
