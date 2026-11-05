package com.autosphere.dashboard.domain.model

data class UserSettings(
    val isDarkMode: Boolean = true,
    val useFahrenheit: Boolean = false,
    val useMph: Boolean = false,
    val displayBrightnessPercent: Int = 85,
    val screenTimeoutMinutes: Int = 30,
    val masterVolumePercent: Int = 45,
    val soundProfile: SoundProfile = SoundProfile.DYNAMIC_SURROUND,
    val isBluetoothEnabled: Boolean = true,
    val isWifiEnabled: Boolean = true,
    val isHotspotEnabled: Boolean = false,
    val isSimulatorDemoMode: Boolean = true
)

enum class SoundProfile(val displayName: String) {
    BALANCED_STUDIO("Balanced Studio"),
    DYNAMIC_SURROUND("Dynamic EV Surround"),
    BASS_BOOST("Electro Bass Boost"),
    VOCAL_FOCUS("Vocal & Podcast Focus")
}
