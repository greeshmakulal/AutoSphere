package com.autosphere.dashboard.presentation.drivingmodes

import androidx.lifecycle.ViewModel
import com.autosphere.dashboard.domain.model.DrivingMode
import com.autosphere.dashboard.domain.model.VehicleState
import com.autosphere.dashboard.domain.usecase.GetVehicleTelemetryUseCase
import kotlinx.coroutines.flow.StateFlow

class DrivingModesViewModel(
    private val telemetryUseCase: GetVehicleTelemetryUseCase
) : ViewModel() {

    val vehicleState: StateFlow<VehicleState> = telemetryUseCase.telemetry

    fun selectDrivingMode(mode: DrivingMode) {
        telemetryUseCase.setDrivingMode(mode)
    }
}
