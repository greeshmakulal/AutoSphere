package com.autosphere.dashboard.presentation.vehicle

import androidx.lifecycle.ViewModel
import com.autosphere.dashboard.domain.model.VehicleState
import com.autosphere.dashboard.domain.usecase.GetVehicleTelemetryUseCase
import com.autosphere.dashboard.domain.usecase.ManageSettingsUseCase
import kotlinx.coroutines.flow.StateFlow

class VehicleViewModel(
    private val telemetryUseCase: GetVehicleTelemetryUseCase,
    private val settingsUseCase: ManageSettingsUseCase
) : ViewModel() {

    val vehicleState: StateFlow<VehicleState> = telemetryUseCase.telemetry
    val userSettings = settingsUseCase.userSettings

    fun toggleHeadlights() = telemetryUseCase.toggleHeadlights()
    fun toggleDoors() = telemetryUseCase.toggleDoorLocks()
    fun toggleParkingBrake() = telemetryUseCase.toggleParkingBrake()
    fun toggleCharging() = telemetryUseCase.setCharging(!vehicleState.value.isCharging)
}
