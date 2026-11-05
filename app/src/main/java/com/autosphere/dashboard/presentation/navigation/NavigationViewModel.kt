package com.autosphere.dashboard.presentation.navigation

import androidx.lifecycle.ViewModel
import com.autosphere.dashboard.domain.model.Destination
import com.autosphere.dashboard.domain.model.NavigationState
import com.autosphere.dashboard.domain.repository.NavigationRepository
import kotlinx.coroutines.flow.StateFlow

class NavigationViewModel(
    private val navigationRepository: NavigationRepository
) : ViewModel() {

    val navigationState: StateFlow<NavigationState> = navigationRepository.navigationState

    fun selectDestination(destination: Destination) {
        navigationRepository.setDestination(destination)
    }

    fun cancelNavigation() {
        navigationRepository.cancelNavigation()
    }
}
