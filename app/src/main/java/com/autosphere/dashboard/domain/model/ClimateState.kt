package com.autosphere.dashboard.domain.model

data class ClimateState(
    val driverTempC: Float = 21.5f,
    val passengerTempC: Float = 22.0f,
    val fanSpeed: Int = 2, // 1 to 5
    val isAcOn: Boolean = true,
    val isAutoMode: Boolean = true,
    val isFrontDefrostOn: Boolean = false,
    val isRearDefrostOn: Boolean = false,
    val driverSeatHeatingLevel: Int = 1, // 0 = Off, 1 = Low, 2 = Med, 3 = High
    val passengerSeatHeatingLevel: Int = 0,
    val isAirRecirculationOn: Boolean = false,
    val isSteeringWheelHeatingOn: Boolean = false,
    val isDualZoneSynced: Boolean = false
) {
    companion object {
        const val MIN_TEMP_C = 16.0f
        const val MAX_TEMP_C = 30.0f
        const val TEMP_STEP_C = 0.5f
    }
}
