package com.autosphere.dashboard.domain.model

data class Destination(
    val id: String,
    val name: String,
    val address: String,
    val distanceKm: Float,
    val estimatedTimeMinutes: Int,
    val category: DestinationCategory = DestinationCategory.RECENT
)

enum class DestinationCategory {
    HOME,
    WORK,
    SUPERCHARGER,
    RECENT,
    FAVORITE
}

enum class ManeuverType {
    TURN_LEFT,
    TURN_RIGHT,
    KEEP_STRAIGHT,
    ROUNDABOUT_EXIT,
    HIGHWAY_MERGE,
    DESTINATION_AHEAD
}

data class NavigationState(
    val isNavigating: Boolean = true,
    val destinationName: String = "Starlight Tech Campus, Silicon Valley",
    val destinationAddress: String = "1000 Innovation Blvd, California",
    val distanceRemainingKm: Float = 14.8f,
    val etaString: String = "18:42",
    val estimatedMinutesRemaining: Int = 18,
    val arrivalBatteryPercent: Int = 78,
    val currentManeuver: ManeuverType = ManeuverType.ROUNDABOUT_EXIT,
    val maneuverInstruction: String = "Take 2nd exit onto Grand Avenue Expressway",
    val distanceToManeuverMeters: Int = 350,
    val speedLimitKmh: Int = 80,
    val currentTrafficLevel: String = "Light Traffic (Fastest Route)",
    val savedDestinations: List<Destination> = defaultDestinations()
) {
    companion object {
        fun defaultDestinations(): List<Destination> = listOf(
            Destination("d1", "Home", "42 Ocean Crest Way, Pacific Coast", 8.2f, 12, DestinationCategory.HOME),
            Destination("d2", "Headquarters", "1000 Innovation Blvd, Silicon Valley", 14.8f, 18, DestinationCategory.WORK),
            Destination("d3", "Tesla Supercharger V4", "Bay Street Superhub, 250kW", 4.1f, 6, DestinationCategory.SUPERCHARGER),
            Destination("d4", "Skyline Scenic Overlook", "Mountain Ridge Pass, Peak 4", 26.5f, 32, DestinationCategory.FAVORITE)
        )
    }
}
