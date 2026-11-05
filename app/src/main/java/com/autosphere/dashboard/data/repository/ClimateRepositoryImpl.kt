package com.autosphere.dashboard.data.repository

import com.autosphere.dashboard.domain.model.ClimateState
import com.autosphere.dashboard.domain.repository.ClimateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClimateRepositoryImpl : ClimateRepository {

    private val _climateState = MutableStateFlow(ClimateState())
    override val climateState: StateFlow<ClimateState> = _climateState.asStateFlow()

    override fun adjustDriverTemp(delta: Float) {
        val current = _climateState.value
        val newDriver = (current.driverTempC + delta).coerceIn(ClimateState.MIN_TEMP_C, ClimateState.MAX_TEMP_C)
        val newPassenger = if (current.isDualZoneSynced) newDriver else current.passengerTempC
        _climateState.value = current.copy(
            driverTempC = newDriver,
            passengerTempC = newPassenger
        )
    }

    override fun adjustPassengerTemp(delta: Float) {
        val current = _climateState.value
        val newPassenger = (current.passengerTempC + delta).coerceIn(ClimateState.MIN_TEMP_C, ClimateState.MAX_TEMP_C)
        _climateState.value = current.copy(
            passengerTempC = newPassenger,
            isDualZoneSynced = false
        )
    }

    override fun setDriverTemp(temp: Float) {
        val current = _climateState.value
        val clamped = temp.coerceIn(ClimateState.MIN_TEMP_C, ClimateState.MAX_TEMP_C)
        _climateState.value = current.copy(
            driverTempC = clamped,
            passengerTempC = if (current.isDualZoneSynced) clamped else current.passengerTempC
        )
    }

    override fun setPassengerTemp(temp: Float) {
        val current = _climateState.value
        val clamped = temp.coerceIn(ClimateState.MIN_TEMP_C, ClimateState.MAX_TEMP_C)
        _climateState.value = current.copy(
            passengerTempC = clamped,
            isDualZoneSynced = false
        )
    }

    override fun setFanSpeed(speed: Int) {
        val current = _climateState.value
        _climateState.value = current.copy(fanSpeed = speed.coerceIn(1, 5))
    }

    override fun toggleAc() {
        val current = _climateState.value
        _climateState.value = current.copy(isAcOn = !current.isAcOn)
    }

    override fun toggleAutoMode() {
        val current = _climateState.value
        val newAuto = !current.isAutoMode
        _climateState.value = current.copy(
            isAutoMode = newAuto,
            fanSpeed = if (newAuto) 2 else current.fanSpeed
        )
    }

    override fun toggleFrontDefrost() {
        val current = _climateState.value
        _climateState.value = current.copy(isFrontDefrostOn = !current.isFrontDefrostOn)
    }

    override fun toggleRearDefrost() {
        val current = _climateState.value
        _climateState.value = current.copy(isRearDefrostOn = !current.isRearDefrostOn)
    }

    override fun cycleDriverSeatHeating() {
        val current = _climateState.value
        val nextLevel = (current.driverSeatHeatingLevel + 1) % 4
        _climateState.value = current.copy(driverSeatHeatingLevel = nextLevel)
    }

    override fun cyclePassengerSeatHeating() {
        val current = _climateState.value
        val nextLevel = (current.passengerSeatHeatingLevel + 1) % 4
        _climateState.value = current.copy(passengerSeatHeatingLevel = nextLevel)
    }

    override fun toggleAirRecirculation() {
        val current = _climateState.value
        _climateState.value = current.copy(isAirRecirculationOn = !current.isAirRecirculationOn)
    }

    override fun toggleSteeringWheelHeating() {
        val current = _climateState.value
        _climateState.value = current.copy(isSteeringWheelHeatingOn = !current.isSteeringWheelHeatingOn)
    }

    override fun toggleDualZoneSync() {
        val current = _climateState.value
        val newSync = !current.isDualZoneSynced
        _climateState.value = current.copy(
            isDualZoneSynced = newSync,
            passengerTempC = if (newSync) current.driverTempC else current.passengerTempC
        )
    }
}
