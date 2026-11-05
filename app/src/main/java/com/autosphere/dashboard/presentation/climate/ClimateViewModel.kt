package com.autosphere.dashboard.presentation.climate

import androidx.lifecycle.ViewModel
import com.autosphere.dashboard.domain.model.ClimateState
import com.autosphere.dashboard.domain.usecase.ControlClimateUseCase
import com.autosphere.dashboard.domain.usecase.ManageSettingsUseCase
import kotlinx.coroutines.flow.StateFlow

class ClimateViewModel(
    private val climateUseCase: ControlClimateUseCase,
    private val settingsUseCase: ManageSettingsUseCase
) : ViewModel() {

    val climateState: StateFlow<ClimateState> = climateUseCase.climateState
    val userSettings = settingsUseCase.userSettings

    fun adjustDriverTemp(delta: Float) = climateUseCase.adjustDriverTemp(delta)
    fun adjustPassengerTemp(delta: Float) = climateUseCase.adjustPassengerTemp(delta)
    fun setFanSpeed(speed: Int) = climateUseCase.setFanSpeed(speed)
    fun toggleAc() = climateUseCase.toggleAc()
    fun toggleAuto() = climateUseCase.toggleAuto()
    fun toggleFrontDefrost() = climateUseCase.toggleFrontDefrost()
    fun toggleRearDefrost() = climateUseCase.toggleRearDefrost()
    fun cycleDriverSeatHeating() = climateUseCase.cycleDriverSeatHeating()
    fun cyclePassengerSeatHeating() = climateUseCase.cyclePassengerSeatHeating()
    fun toggleAirRecirculation() = climateUseCase.toggleAirRecirculation()
    fun toggleSteeringHeat() = climateUseCase.toggleSteeringHeat()
    fun toggleDualSync() = climateUseCase.toggleDualSync()
}
