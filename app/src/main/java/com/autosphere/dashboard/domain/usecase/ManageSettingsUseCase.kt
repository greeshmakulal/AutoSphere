package com.autosphere.dashboard.domain.usecase

import com.autosphere.dashboard.domain.model.SoundProfile
import com.autosphere.dashboard.domain.model.UserSettings
import com.autosphere.dashboard.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow

class ManageSettingsUseCase(
    private val repository: SettingsRepository
) {
    val userSettings: StateFlow<UserSettings> = repository.userSettings

    suspend fun setDarkMode(isDark: Boolean) = repository.updateDarkMode(isDark)
    suspend fun setUseFahrenheit(useF: Boolean) = repository.updateUseFahrenheit(useF)
    suspend fun setUseMph(useMph: Boolean) = repository.updateUseMph(useMph)
    suspend fun setBrightness(brightness: Int) = repository.updateBrightness(brightness)
    suspend fun setVolume(volume: Int) = repository.updateVolume(volume)
    suspend fun setSoundProfile(profile: SoundProfile) = repository.updateSoundProfile(profile)
    suspend fun setBluetooth(enabled: Boolean) = repository.updateBluetooth(enabled)
    suspend fun setWifi(enabled: Boolean) = repository.updateWifi(enabled)
    suspend fun setHotspot(enabled: Boolean) = repository.updateHotspot(enabled)
    suspend fun setDemoMode(enabled: Boolean) = repository.updateDemoMode(enabled)
}
