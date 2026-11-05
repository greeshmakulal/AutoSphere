package com.autosphere.dashboard.data.repository

import com.autosphere.dashboard.domain.model.Destination
import com.autosphere.dashboard.domain.model.ManeuverType
import com.autosphere.dashboard.domain.model.NavigationState
import com.autosphere.dashboard.domain.repository.NavigationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationRepositoryImpl : NavigationRepository {

    private val _navigationState = MutableStateFlow(NavigationState())
    override val navigationState: StateFlow<NavigationState> = _navigationState.asStateFlow()

    override fun setDestination(destination: Destination) {
        val current = _navigationState.value
        _navigationState.value = current.copy(
            isNavigating = true,
            destinationName = destination.name,
            destinationAddress = destination.address,
            distanceRemainingKm = destination.distanceKm,
            estimatedMinutesRemaining = destination.estimatedTimeMinutes,
            currentManeuver = ManeuverType.ROUNDABOUT_EXIT,
            maneuverInstruction = "Follow navigation route to ${destination.name}",
            distanceToManeuverMeters = 400
        )
    }

    override fun cancelNavigation() {
        val current = _navigationState.value
        _navigationState.value = current.copy(
            isNavigating = false,
            destinationName = "No active destination",
            destinationAddress = "Tap a destination or search to route",
            distanceRemainingKm = 0f,
            estimatedMinutesRemaining = 0
        )
    }
}
