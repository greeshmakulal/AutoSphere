package com.autosphere.dashboard.domain.repository

import com.autosphere.dashboard.domain.model.DrivingMode
import com.autosphere.dashboard.domain.model.Gear
import com.autosphere.dashboard.domain.model.VehicleState
import kotlinx.coroutines.flow.StateFlow

interface VehicleRepository {
    val vehicleState: StateFlow<VehicleState>
    fun setGear(gear: Gear)
    fun toggleHeadlights()
    fun toggleDoorLocks()
    fun toggleParkingBrake()
    fun setDrivingMode(mode: DrivingMode)
    fun setDemoMode(enabled: Boolean)
    fun setSimulatedSpeed(speedKmh: Float)
    fun setCharging(isCharging: Boolean)
}
