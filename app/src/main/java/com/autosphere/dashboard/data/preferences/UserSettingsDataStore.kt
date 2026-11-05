package com.autosphere.dashboard.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.autosphere.dashboard.domain.model.SoundProfile
import com.autosphere.dashboard.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "autosphere_preferences")

class UserSettingsDataStore(private val context: Context) {

    private object PreferencesKeys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val USE_FAHRENHEIT = booleanPreferencesKey("use_fahrenheit")
        val USE_MPH = booleanPreferencesKey("use_mph")
        val BRIGHTNESS = intPreferencesKey("brightness")
        val SCREEN_TIMEOUT = intPreferencesKey("screen_timeout")
        val MASTER_VOLUME = intPreferencesKey("master_volume")
        val SOUND_PROFILE = stringPreferencesKey("sound_profile")
        val BLUETOOTH_ENABLED = booleanPreferencesKey("bluetooth_enabled")
        val WIFI_ENABLED = booleanPreferencesKey("wifi_enabled")
        val HOTSPOT_ENABLED = booleanPreferencesKey("hotspot_enabled")
        val DEMO_MODE = booleanPreferencesKey("demo_mode")
    }

    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            isDarkMode = preferences[PreferencesKeys.DARK_MODE] ?: true,
            useFahrenheit = preferences[PreferencesKeys.USE_FAHRENHEIT] ?: false,
            useMph = preferences[PreferencesKeys.USE_MPH] ?: false,
            displayBrightnessPercent = preferences[PreferencesKeys.BRIGHTNESS] ?: 85,
            screenTimeoutMinutes = preferences[PreferencesKeys.SCREEN_TIMEOUT] ?: 30,
            masterVolumePercent = preferences[PreferencesKeys.MASTER_VOLUME] ?: 45,
            soundProfile = preferences[PreferencesKeys.SOUND_PROFILE]?.let {
                try {
                    SoundProfile.valueOf(it)
                } catch (e: Exception) {
                    SoundProfile.DYNAMIC_SURROUND
                }
            } ?: SoundProfile.DYNAMIC_SURROUND,
            isBluetoothEnabled = preferences[PreferencesKeys.BLUETOOTH_ENABLED] ?: true,
            isWifiEnabled = preferences[PreferencesKeys.WIFI_ENABLED] ?: true,
            isHotspotEnabled = preferences[PreferencesKeys.HOTSPOT_ENABLED] ?: false,
            isSimulatorDemoMode = preferences[PreferencesKeys.DEMO_MODE] ?: true
        )
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.DARK_MODE] = enabled }
    }

    suspend fun setUseFahrenheit(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.USE_FAHRENHEIT] = enabled }
    }

    suspend fun setUseMph(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.USE_MPH] = enabled }
    }

    suspend fun setBrightness(brightness: Int) {
        context.dataStore.edit { it[PreferencesKeys.BRIGHTNESS] = brightness }
    }

    suspend fun setMasterVolume(volume: Int) {
        context.dataStore.edit { it[PreferencesKeys.MASTER_VOLUME] = volume }
    }

    suspend fun setSoundProfile(profile: SoundProfile) {
        context.dataStore.edit { it[PreferencesKeys.SOUND_PROFILE] = profile.name }
    }

    suspend fun setBluetooth(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.BLUETOOTH_ENABLED] = enabled }
    }

    suspend fun setWifi(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.WIFI_ENABLED] = enabled }
    }

    suspend fun setHotspot(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.HOTSPOT_ENABLED] = enabled }
    }

    suspend fun setDemoMode(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.DEMO_MODE] = enabled }
    }
}
