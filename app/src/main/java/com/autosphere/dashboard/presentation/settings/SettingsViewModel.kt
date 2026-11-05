package com.autosphere.dashboard.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autosphere.dashboard.domain.model.SoundProfile
import com.autosphere.dashboard.domain.model.UserSettings
import com.autosphere.dashboard.domain.usecase.GetVehicleTelemetryUseCase
import com.autosphere.dashboard.domain.usecase.ManageSettingsUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsUseCase: ManageSettingsUseCase,
    private val telemetryUseCase: GetVehicleTelemetryUseCase
) : ViewModel() {

    val userSettings: StateFlow<UserSettings> = settingsUseCase.userSettings
    val vehicleState = telemetryUseCase.telemetry

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch { settingsUseCase.setDarkMode(enabled) }
    }

    fun toggleUseFahrenheit(enabled: Boolean) {
        viewModelScope.launch { settingsUseCase.setUseFahrenheit(enabled) }
    }

    fun toggleUseMph(enabled: Boolean) {
        viewModelScope.launch { settingsUseCase.setUseMph(enabled) }
    }

    fun setBrightness(brightness: Int) {
        viewModelScope.launch { settingsUseCase.setBrightness(brightness) }
    }

    fun setMasterVolume(volume: Int) {
        viewModelScope.launch { settingsUseCase.setVolume(volume) }
    }

    fun setSoundProfile(profile: SoundProfile) {
        viewModelScope.launch { settingsUseCase.setSoundProfile(profile) }
    }

    fun toggleBluetooth(enabled: Boolean) {
        viewModelScope.launch { settingsUseCase.setBluetooth(enabled) }
    }

    fun toggleWifi(enabled: Boolean) {
        viewModelScope.launch { settingsUseCase.setWifi(enabled) }
    }

    fun toggleHotspot(enabled: Boolean) {
        viewModelScope.launch { settingsUseCase.setHotspot(enabled) }
    }

    fun toggleDemoMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsUseCase.setDemoMode(enabled)
            telemetryUseCase.setDemoMode(enabled)
        }
    }
}
