package com.autosphere.dashboard.domain.model

enum class Gear(val display: String) {
    PARK("P"),
    REVERSE("R"),
    NEUTRAL("N"),
    DRIVE("D")
}

data class TirePressure(
    val frontLeftPsi: Float = 35.0f,
    val frontRightPsi: Float = 35.2f,
    val rearLeftPsi: Float = 34.8f,
    val rearRightPsi: Float = 35.1f,
    val targetPsi: Float = 35.0f
) {
    val isAllNormal: Boolean
        get() = frontLeftPsi in 32.0f..38.0f &&
                frontRightPsi in 32.0f..38.0f &&
                rearLeftPsi in 32.0f..38.0f &&
                rearRightPsi in 32.0f..38.0f
}

data class VehicleState(
    val speedKmh: Float = 0f,
    val rpm: Float = 0f,
    val gear: Gear = Gear.PARK,
    val batteryPercent: Int = 84,
    val estimatedRangeKm: Int = 412,
    val odometerKm: Float = 14820f,
    val outsideTempC: Float = 21.5f,
    val tirePressure: TirePressure = TirePressure(),
    val drivingMode: DrivingMode = DrivingMode.COMFORT,
    val isHeadlightsOn: Boolean = true,
    val isDoorsLocked: Boolean = true,
    val isParkingBrakeEngaged: Boolean = true,
    val isCharging: Boolean = false,
    val chargingPowerKw: Float = 0f,
    val energyConsumptionWhPerKm: Int = 168,
    val motorTempC: Float = 62f,
    val batteryVoltage: Float = 398.5f,
    val isConnectedToCarService: Boolean = false,
    val isDemoMode: Boolean = true,
    val unavailableProperties: Set<String> = emptySet()
)
