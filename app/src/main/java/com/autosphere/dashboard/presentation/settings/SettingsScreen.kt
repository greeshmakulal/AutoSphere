package com.autosphere.dashboard.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autosphere.dashboard.domain.model.SoundProfile
import com.autosphere.dashboard.presentation.components.GlassCard
import com.autosphere.dashboard.presentation.theme.DarkBackground
import com.autosphere.dashboard.presentation.theme.DarkSurfaceVariant
import com.autosphere.dashboard.presentation.theme.ElectricBlue
import com.autosphere.dashboard.presentation.theme.NeonCyan
import com.autosphere.dashboard.presentation.theme.PulsePurple
import com.autosphere.dashboard.presentation.theme.StatusAmber
import com.autosphere.dashboard.presentation.theme.StatusGreen
import com.autosphere.dashboard.presentation.theme.TextMutedDark
import com.autosphere.dashboard.presentation.theme.TextPrimaryDark

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.userSettings.collectAsState()
    val vehicleState by viewModel.vehicleState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        // Header
        Text(
            text = "SYSTEM SETTINGS",
            color = TextPrimaryDark,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(18.dp))

        // Row 1: Display & Units
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Display Settings Card
            GlassCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SectionHeader("DISPLAY & THEME", Icons.Default.BrightnessMedium)

                    Spacer(Modifier.height(14.dp))

                    SettingToggleRow(
                        title = "Dark Cockpit Mode",
                        subtitle = "High contrast OLED optimized palette",
                        checked = settings.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode(it) }
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Display Brightness: ${settings.displayBrightnessPercent}%",
                        color = TextPrimaryDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Slider(
                        value = settings.displayBrightnessPercent.toFloat(),
                        onValueChange = { viewModel.setBrightness(it.toInt()) },
                        valueRange = 20f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricBlue,
                            activeTrackColor = ElectricBlue,
                            inactiveTrackColor = Color(0xFF334155)
                        )
                    )
                }
            }

            // Units & Measurements Card
            GlassCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SectionHeader("UNITS & LOCALIZATION", Icons.Default.Speed)

                    Spacer(Modifier.height(14.dp))

                    SettingToggleRow(
                        title = "Imperial Speed Units (MPH)",
                        subtitle = if (settings.useMph) "Speed shown in Miles per Hour" else "Speed shown in Kilometers per Hour (KM/H)",
                        checked = settings.useMph,
                        onCheckedChange = { viewModel.toggleUseMph(it) }
                    )

                    Spacer(Modifier.height(14.dp))

                    SettingToggleRow(
                        title = "Fahrenheit Temperature (°F)",
                        subtitle = if (settings.useFahrenheit) "Cabin & exterior temp shown in °F" else "Cabin & exterior temp shown in Celsius (°C)",
                        checked = settings.useFahrenheit,
                        onCheckedChange = { viewModel.toggleUseFahrenheit(it) }
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Row 2: Sound & Wireless Connectivity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Sound Settings Card
            GlassCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SectionHeader("PREMIUM AUDIO & EQUALIZER", Icons.Default.GraphicEq)

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "Master Volume: ${settings.masterVolumePercent}%",
                        color = TextPrimaryDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Slider(
                        value = settings.masterVolumePercent.toFloat(),
                        onValueChange = { viewModel.setMasterVolume(it.toInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = PulsePurple,
                            activeTrackColor = PulsePurple,
                            inactiveTrackColor = Color(0xFF334155)
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Acoustic Tuning Profile",
                        color = TextMutedDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SoundProfile.values().forEach { profile ->
                            val isSelected = settings.soundProfile == profile
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) PulsePurple.copy(alpha = 0.25f)
                                        else DarkSurfaceVariant
                                    )
                                    .clickable { viewModel.setSoundProfile(profile) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = profile.displayName.split(" ").first(),
                                    color = if (isSelected) PulsePurple else TextMutedDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Connectivity Card
            GlassCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SectionHeader("WIRELESS NETWORKS", Icons.Default.NetworkWifi)

                    Spacer(Modifier.height(14.dp))

                    SettingToggleRow(
                        title = "Bluetooth Cockpit Link",
                        subtitle = "Audio streaming & hands-free calling",
                        checked = settings.isBluetoothEnabled,
                        onCheckedChange = { viewModel.toggleBluetooth(it) }
                    )

                    Spacer(Modifier.height(10.dp))

                    SettingToggleRow(
                        title = "Wi-Fi Connectivity",
                        subtitle = "Over-the-air firmware & map streaming",
                        checked = settings.isWifiEnabled,
                        onCheckedChange = { viewModel.toggleWifi(it) }
                    )

                    Spacer(Modifier.height(10.dp))

                    SettingToggleRow(
                        title = "Cabin 5G Hotspot",
                        subtitle = "Passenger device internet sharing",
                        checked = settings.isHotspotEnabled,
                        onCheckedChange = { viewModel.toggleHotspot(it) }
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Row 3: Developer / Simulator Mode & About AutoSphere
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Simulator Mode Card
            GlassCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SectionHeader("AAOS HARDWARE & SIMULATOR", Icons.Default.DeveloperMode)

                    Spacer(Modifier.height(14.dp))

                    SettingToggleRow(
                        title = "Vehicle Telemetry Simulator",
                        subtitle = if (settings.isSimulatorDemoMode)
                            "Running autonomous EV simulation engine"
                        else
                            "Connecting directly to AAOS CarPropertyManager",
                        checked = settings.isSimulatorDemoMode,
                        onCheckedChange = { viewModel.toggleDemoMode(it) }
                    )

                    Spacer(Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceVariant)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "CAR SERVICE STATUS",
                                color = TextMutedDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = if (vehicleState.isConnectedToCarService)
                                    "Connected to Android Automotive OS Car Service"
                                else
                                    "Hardware HAL disconnected (Simulator Active)",
                                color = if (vehicleState.isConnectedToCarService) StatusGreen else StatusAmber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // About AutoSphere Card
            GlassCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SectionHeader("ABOUT AUTOSPHERE", Icons.Default.Info)

                    Spacer(Modifier.height(14.dp))

                    AboutDetailRow("Application ID", "com.autosphere.dashboard")
                    AboutDetailRow("Version", "1.0.0-PROD (Build 2026)")
                    AboutDetailRow("Target Platform", "Android Automotive OS (AAOS)")
                    AboutDetailRow("Architecture", "MVVM + Clean Architecture")
                    AboutDetailRow("Design System", "Material 3 + AutoSphere Glassmorphism")
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ElectricBlue,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            color = ElectricBlue,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextPrimaryDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = TextMutedDark,
                fontSize = 11.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = ElectricBlue,
                uncheckedThumbColor = TextMutedDark,
                uncheckedTrackColor = DarkSurfaceVariant
            )
        )
    }
}

@Composable
private fun AboutDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextMutedDark, fontSize = 12.sp)
        Text(text = value, color = TextPrimaryDark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
