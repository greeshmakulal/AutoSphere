package com.autosphere.dashboard.domain.repository

import com.autosphere.dashboard.domain.model.ClimateState
import kotlinx.coroutines.flow.StateFlow

interface ClimateRepository {
    val climateState: StateFlow<ClimateState>
    fun adjustDriverTemp(delta: Float)
    fun adjustPassengerTemp(delta: Float)
    fun setDriverTemp(temp: Float)
    fun setPassengerTemp(temp: Float)
    fun setFanSpeed(speed: Int)
    fun toggleAc()
    fun toggleAutoMode()
    fun toggleFrontDefrost()
    fun toggleRearDefrost()
    fun cycleDriverSeatHeating()
    fun cyclePassengerSeatHeating()
    fun toggleAirRecirculation()
    fun toggleSteeringWheelHeating()
    fun toggleDualZoneSync()
}
