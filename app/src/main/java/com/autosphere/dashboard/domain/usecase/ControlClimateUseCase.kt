package com.autosphere.dashboard.domain.usecase

import com.autosphere.dashboard.domain.model.ClimateState
import com.autosphere.dashboard.domain.repository.ClimateRepository
import kotlinx.coroutines.flow.StateFlow

class ControlClimateUseCase(
    private val repository: ClimateRepository
) {
    val climateState: StateFlow<ClimateState> = repository.climateState

    fun adjustDriverTemp(delta: Float) = repository.adjustDriverTemp(delta)
    fun adjustPassengerTemp(delta: Float) = repository.adjustPassengerTemp(delta)
    fun setFanSpeed(speed: Int) = repository.setFanSpeed(speed)
    fun toggleAc() = repository.toggleAc()
    fun toggleAuto() = repository.toggleAutoMode()
    fun toggleFrontDefrost() = repository.toggleFrontDefrost()
    fun toggleRearDefrost() = repository.toggleRearDefrost()
    fun cycleDriverSeatHeating() = repository.cycleDriverSeatHeating()
    fun cyclePassengerSeatHeating() = repository.cyclePassengerSeatHeating()
    fun toggleAirRecirculation() = repository.toggleAirRecirculation()
    fun toggleSteeringHeat() = repository.toggleSteeringWheelHeating()
    fun toggleDualSync() = repository.toggleDualZoneSync()
}
