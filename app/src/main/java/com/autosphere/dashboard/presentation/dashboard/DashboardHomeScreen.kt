package com.autosphere.dashboard.presentation.dashboard

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.Icon
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
import com.autosphere.dashboard.domain.model.Gear
import com.autosphere.dashboard.presentation.components.BatteryRangeWidget
import com.autosphere.dashboard.presentation.components.ClimateQuickWidget
import com.autosphere.dashboard.presentation.components.GearSelectorWidget
import com.autosphere.dashboard.presentation.components.GlassCard
import com.autosphere.dashboard.presentation.components.MediaMiniWidget
import com.autosphere.dashboard.presentation.components.SpeedometerGauge
import com.autosphere.dashboard.presentation.components.TopStatusBar
import com.autosphere.dashboard.presentation.components.VehicleIllustration
import com.autosphere.dashboard.presentation.theme.DarkBackground
import com.autosphere.dashboard.presentation.theme.DarkSurfaceVariant
import com.autosphere.dashboard.presentation.theme.ElectricBlue
import com.autosphere.dashboard.presentation.theme.NeonCyan
import com.autosphere.dashboard.presentation.theme.PulsePurple
import com.autosphere.dashboard.presentation.theme.StatusAmber
import com.autosphere.dashboard.presentation.theme.StatusGreen
import com.autosphere.dashboard.presentation.theme.StatusRed
import com.autosphere.dashboard.presentation.theme.TextMutedDark
import com.autosphere.dashboard.presentation.theme.TextPrimaryDark

@Composable
fun DashboardHomeScreen(
    viewModel: DashboardViewModel,
    onNavigateToMedia: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vehicleState by viewModel.vehicleState.collectAsState()
    val climateState by viewModel.climateState.collectAsState()
    val mediaState by viewModel.mediaState.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top Status Bar
        TopStatusBar(
            vehicleState = vehicleState,
            useFahrenheit = userSettings.useFahrenheit
        )

        // Main Cockpit Grid Layout (Optimized for landscape automotive display)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Left Column: Driving Overview & Vehicle Illustration
            Column(
                modifier = Modifier
                    .weight(1.1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Vehicle Speedometer + PRND
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpeedometerGauge(
                        speed = vehicleState.speedKmh,
                        useMph = userSettings.useMph,
                        drivingMode = vehicleState.drivingMode,
                        size = 230.dp
                    )

                    // Vehicle Illustration & Quick Toggles
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        VehicleIllustration(
                            isHeadlightsOn = vehicleState.isHeadlightsOn,
                            isDoorsLocked = vehicleState.isDoorsLocked,
                            isCharging = vehicleState.isCharging,
                            size = 175.dp
                        )

                        GearSelectorWidget(
                            currentGear = vehicleState.gear,
                            onGearSelected = { viewModel.selectGear(it) }
                        )
                    }
                }

                // Quick Action Bar (Lights, Locks, Parking Brake, Charge Port)
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuickActionButton(
                            icon = Icons.Default.Highlight,
                            label = if (vehicleState.isHeadlightsOn) "LIGHTS ON" else "LIGHTS OFF",
                            isActive = vehicleState.isHeadlightsOn,
                            activeColor = NeonCyan,
                            onClick = { viewModel.toggleHeadlights() }
                        )

                        QuickActionButton(
                            icon = if (vehicleState.isDoorsLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            label = if (vehicleState.isDoorsLocked) "LOCKED" else "UNLOCKED",
                            isActive = vehicleState.isDoorsLocked,
                            activeColor = ElectricBlue,
                            onClick = { viewModel.toggleDoorLocks() }
                        )

                        QuickActionButton(
                            icon = Icons.Default.PanTool,
                            label = if (vehicleState.isParkingBrakeEngaged) "P-BRAKE ON" else "P-BRAKE OFF",
                            isActive = vehicleState.isParkingBrakeEngaged,
                            activeColor = StatusRed,
                            onClick = { viewModel.toggleParkingBrake() }
                        )
                    }
                }
            }

            // Right Column: Battery, Climate Summary & Media Player
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // High Voltage Battery & Range Card
                BatteryRangeWidget(
                    batteryPercent = vehicleState.batteryPercent,
                    estimatedRangeKm = vehicleState.estimatedRangeKm,
                    isCharging = vehicleState.isCharging,
                    chargingPowerKw = vehicleState.chargingPowerKw,
                    energyConsumptionWhPerKm = vehicleState.energyConsumptionWhPerKm,
                    useMph = userSettings.useMph
                )

                // Climate Quick Controls
                ClimateQuickWidget(
                    climateState = climateState,
                    useFahrenheit = userSettings.useFahrenheit,
                    onAdjustDriverTemp = { viewModel.adjustDriverTemp(it) },
                    onAdjustPassengerTemp = { viewModel.adjustPassengerTemp(it) },
                    onToggleAc = { viewModel.toggleAc() },
                    onToggleAuto = { viewModel.toggleAutoClimate() },
                    onToggleFrontDefrost = { viewModel.toggleFrontDefrost() },
                    onCycleDriverSeatHeat = { viewModel.cycleDriverSeatHeating() }
                )

                // Media Player Mini Widget
                MediaMiniWidget(
                    mediaState = mediaState,
                    onTogglePlayPause = { viewModel.toggleMediaPlayPause() },
                    onPlayNext = { viewModel.playNextTrack() },
                    onPlayPrevious = { viewModel.playPreviousTrack() },
                    onOpenMediaScreen = onNavigateToMedia
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isActive) activeColor.copy(alpha = 0.15f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) activeColor else TextMutedDark,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = label,
            color = if (isActive) TextPrimaryDark else TextMutedDark,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}
