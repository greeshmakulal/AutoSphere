package com.autosphere.dashboard.domain.repository

import com.autosphere.dashboard.domain.model.SoundProfile
import com.autosphere.dashboard.domain.model.UserSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val userSettings: StateFlow<UserSettings>
    suspend fun updateDarkMode(isDark: Boolean)
    suspend fun updateUseFahrenheit(useF: Boolean)
    suspend fun updateUseMph(useMph: Boolean)
    suspend fun updateBrightness(brightness: Int)
    suspend fun updateVolume(volume: Int)
    suspend fun updateSoundProfile(profile: SoundProfile)
    suspend fun updateBluetooth(enabled: Boolean)
    suspend fun updateWifi(enabled: Boolean)
    suspend fun updateHotspot(enabled: Boolean)
    suspend fun updateDemoMode(enabled: Boolean)
}
