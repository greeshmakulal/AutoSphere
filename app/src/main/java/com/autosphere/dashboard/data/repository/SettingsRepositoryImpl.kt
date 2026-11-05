package com.autosphere.dashboard.data.repository

import com.autosphere.dashboard.data.preferences.UserSettingsDataStore
import com.autosphere.dashboard.domain.model.SoundProfile
import com.autosphere.dashboard.domain.model.UserSettings
import com.autosphere.dashboard.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsRepositoryImpl(
    private val dataStore: UserSettingsDataStore,
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : SettingsRepository {

    override val userSettings: StateFlow<UserSettings> = dataStore.userSettingsFlow.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = UserSettings()
    )

    override suspend fun updateDarkMode(isDark: Boolean) {
        dataStore.setDarkMode(isDark)
    }

    override suspend fun updateUseFahrenheit(useF: Boolean) {
        dataStore.setUseFahrenheit(useF)
    }

    override suspend fun updateUseMph(useMph: Boolean) {
        dataStore.setUseMph(useMph)
    }

    override suspend fun updateBrightness(brightness: Int) {
        dataStore.setBrightness(brightness)
    }

    override suspend fun updateVolume(volume: Int) {
        dataStore.setMasterVolume(volume)
    }

    override suspend fun updateSoundProfile(profile: SoundProfile) {
        dataStore.setSoundProfile(profile)
    }

    override suspend fun updateBluetooth(enabled: Boolean) {
        dataStore.setBluetooth(enabled)
    }

    override suspend fun updateWifi(enabled: Boolean) {
        dataStore.setWifi(enabled)
    }

    override suspend fun updateHotspot(enabled: Boolean) {
        dataStore.setHotspot(enabled)
    }

    override suspend fun updateDemoMode(enabled: Boolean) {
        dataStore.setDemoMode(enabled)
    }
}
