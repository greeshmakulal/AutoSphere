package com.autosphere.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autosphere.dashboard.domain.model.ClimateState
import com.autosphere.dashboard.domain.model.Gear
import com.autosphere.dashboard.domain.model.MediaState
import com.autosphere.dashboard.domain.model.VehicleState
import com.autosphere.dashboard.domain.usecase.ControlClimateUseCase
import com.autosphere.dashboard.domain.usecase.GetVehicleTelemetryUseCase
import com.autosphere.dashboard.domain.usecase.ManageSettingsUseCase
import com.autosphere.dashboard.domain.usecase.MediaPlaybackUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val telemetryUseCase: GetVehicleTelemetryUseCase,
    private val climateUseCase: ControlClimateUseCase,
    private val mediaUseCase: MediaPlaybackUseCase,
    private val settingsUseCase: ManageSettingsUseCase
) : ViewModel() {

    val vehicleState: StateFlow<VehicleState> = telemetryUseCase.telemetry
    val climateState: StateFlow<ClimateState> = climateUseCase.climateState
    val mediaState: StateFlow<MediaState> = mediaUseCase.mediaState
    val userSettings = settingsUseCase.userSettings

    fun selectGear(gear: Gear) {
        telemetryUseCase.setGear(gear)
    }

    fun toggleHeadlights() {
        telemetryUseCase.toggleHeadlights()
    }

    fun toggleDoorLocks() {
        telemetryUseCase.toggleDoorLocks()
    }

    fun toggleParkingBrake() {
        telemetryUseCase.toggleParkingBrake()
    }

    fun adjustDriverTemp(delta: Float) {
        climateUseCase.adjustDriverTemp(delta)
    }

    fun adjustPassengerTemp(delta: Float) {
        climateUseCase.adjustPassengerTemp(delta)
    }

    fun toggleAc() {
        climateUseCase.toggleAc()
    }

    fun toggleAutoClimate() {
        climateUseCase.toggleAuto()
    }

    fun toggleFrontDefrost() {
        climateUseCase.toggleFrontDefrost()
    }

    fun cycleDriverSeatHeating() {
        climateUseCase.cycleDriverSeatHeating()
    }

    fun toggleMediaPlayPause() {
        mediaUseCase.togglePlayPause()
    }

    fun playNextTrack() {
        mediaUseCase.playNext()
    }

    fun playPreviousTrack() {
        mediaUseCase.playPrevious()
    }
}
