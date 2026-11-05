package com.autosphere.dashboard.domain.model

enum class DrivingMode(
    val title: String,
    val description: String,
    val throttleResponse: String,
    val suspension: String,
    val regenBraking: String,
    val steeringFeel: String,
    val efficiencyScore: Int
) {
    ECO(
        title = "Eco",
        description = "Optimized energy efficiency, smooth acceleration, and maximum regenerative braking.",
        throttleResponse = "Gentle & Linear",
        suspension = "Comfort Soft",
        regenBraking = "Maximum (One-Pedal)",
        steeringFeel = "Light & Effortless",
        efficiencyScore = 95
    ),
    COMFORT(
        title = "Comfort",
        description = "Balanced luxury cruising with responsive acceleration and refined chassis dampening.",
        throttleResponse = "Balanced & Direct",
        suspension = "Adaptive Dynamic",
        regenBraking = "Moderate Standard",
        steeringFeel = "Progressive Natural",
        efficiencyScore = 80
    ),
    SPORT(
        title = "Sport",
        description = "Maximum dual-motor torque delivery, firm active dampers, and razor-sharp steering feedback.",
        throttleResponse = "Instant Track Response",
        suspension = "Stiff Track Dynamic",
        regenBraking = "Low (High Coasting)",
        steeringFeel = "Firm & High Feedback",
        efficiencyScore = 60
    )
}
