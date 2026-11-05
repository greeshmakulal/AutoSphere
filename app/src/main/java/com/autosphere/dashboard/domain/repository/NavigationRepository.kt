package com.autosphere.dashboard.domain.repository

import com.autosphere.dashboard.domain.model.Destination
import com.autosphere.dashboard.domain.model.NavigationState
import kotlinx.coroutines.flow.StateFlow

interface NavigationRepository {
    val navigationState: StateFlow<NavigationState>
    fun setDestination(destination: Destination)
    fun cancelNavigation()
}
