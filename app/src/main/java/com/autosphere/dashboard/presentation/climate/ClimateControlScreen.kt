package com.autosphere.dashboard.presentation.climate

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.HeatPump
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.SevereCold
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.autosphere.dashboard.domain.model.ClimateState
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
fun ClimateControlScreen(
    viewModel: ClimateViewModel,
    modifier: Modifier = Modifier
) {
    val climateState by viewModel.climateState.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()

    fun formatTemp(celsius: Float): String {
        return if (userSettings.useFahrenheit) {
            "%.1f°F".format((celsius * 9f / 5f) + 32f)
        } else {
            "%.1f°C".format(celsius)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CABIN CLIMATE",
                color = TextPrimaryDark,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Air Quality Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(StatusGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "HEPA CLEAN • PM2.5: 8 µg/m³",
                        color = StatusGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Dual Sync Toggle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (climateState.isDualZoneSynced) ElectricBlue.copy(alpha = 0.2f)
                            else DarkSurfaceVariant
                        )
                        .clickable { viewModel.toggleDualSync() }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (climateState.isDualZoneSynced) Icons.Default.Link else Icons.Default.LinkOff,
                            contentDescription = "Sync",
                            tint = if (climateState.isDualZoneSynced) ElectricBlue else TextMutedDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (climateState.isDualZoneSynced) "DUAL SYNCED" else "SYNC ZONES",
                            color = if (climateState.isDualZoneSynced) ElectricBlue else TextMutedDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Main Layout: Dual Temperature Dials + Controls
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Driver Zone Dial Card
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp)
            ) {
                ZoneTemperatureControl(
                    zoneTitle = "DRIVER ZONE",
                    tempFormatted = formatTemp(climateState.driverTempC),
                    seatHeatLevel = climateState.driverSeatHeatingLevel,
                    onTempUp = { viewModel.adjustDriverTemp(0.5f) },
                    onTempDown = { viewModel.adjustDriverTemp(-0.5f) },
                    onCycleSeatHeat = { viewModel.cycleDriverSeatHeating() }
                )
            }

            // Center Panel: Fan Speed & System Toggles
            GlassCard(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // System Primary Toggles (AC, AUTO, RECIRC)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SystemToggleItem(
                            title = "AC SYSTEM",
                            isActive = climateState.isAcOn,
                            icon = Icons.Default.AcUnit,
                            activeColor = NeonCyan,
                            onClick = { viewModel.toggleAc() }
                        )

                        SystemToggleItem(
                            title = "AUTO CLIMATE",
                            isActive = climateState.isAutoMode,
                            icon = Icons.Default.Autorenew,
                            activeColor = ElectricBlue,
                            onClick = { viewModel.toggleAuto() }
                        )

                        SystemToggleItem(
                            title = "RECIRCULATION",
                            isActive = climateState.isAirRecirculationOn,
                            icon = Icons.Default.RotateLeft,
                            activeColor = PulsePurple,
                            onClick = { viewModel.toggleAirRecirculation() }
                        )
                    }

                    // Fan Speed Slider
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "BLOWER SPEED",
                                color = TextMutedDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "LEVEL ${climateState.fanSpeed} / 5",
                                color = NeonCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Slider(
                            value = climateState.fanSpeed.toFloat(),
                            onValueChange = { viewModel.setFanSpeed(it.toInt()) },
                            valueRange = 1f..5f,
                            steps = 3,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonCyan,
                                activeTrackColor = NeonCyan,
                                inactiveTrackColor = Color(0xFF334155)
                            )
                        )
                    }

                    // Defrost & Auxiliary Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AuxControlButton(
                            title = "FRONT DEFROST",
                            isActive = climateState.isFrontDefrostOn,
                            icon = Icons.Default.SevereCold,
                            activeColor = ElectricBlue,
                            onClick = { viewModel.toggleFrontDefrost() },
                            modifier = Modifier.weight(1f)
                        )

                        AuxControlButton(
                            title = "REAR DEFROST",
                            isActive = climateState.isRearDefrostOn,
                            icon = Icons.Default.Waves,
                            activeColor = ElectricBlue,
                            onClick = { viewModel.toggleRearDefrost() },
                            modifier = Modifier.weight(1f)
                        )

                        AuxControlButton(
                            title = "STEERING HEAT",
                            isActive = climateState.isSteeringWheelHeatingOn,
                            icon = Icons.Default.HeatPump,
                            activeColor = StatusAmber,
                            onClick = { viewModel.toggleSteeringHeat() },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Passenger Zone Dial Card
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp)
            ) {
                ZoneTemperatureControl(
                    zoneTitle = "PASSENGER ZONE",
                    tempFormatted = formatTemp(climateState.passengerTempC),
                    seatHeatLevel = climateState.passengerSeatHeatingLevel,
                    onTempUp = { viewModel.adjustPassengerTemp(0.5f) },
                    onTempDown = { viewModel.adjustPassengerTemp(-0.5f) },
                    onCycleSeatHeat = { viewModel.cyclePassengerSeatHeating() }
                )
            }
        }
    }
}

@Composable
private fun ZoneTemperatureControl(
    zoneTitle: String,
    tempFormatted: String,
    seatHeatLevel: Int,
    onTempUp: () -> Unit,
    onTempDown: () -> Unit,
    onCycleSeatHeat: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = zoneTitle,
            color = TextMutedDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        // Large Temperature Display with +/-
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
                    .clickable(onClick = onTempUp),
                contentAlignment = Alignment.Center
            ) {
                Text("+", color = TextPrimaryDark, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = tempFormatted,
                color = TextPrimaryDark,
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
                    .clickable(onClick = onTempDown),
                contentAlignment = Alignment.Center
            ) {
                Text("−", color = TextPrimaryDark, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Seat Heating Button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (seatHeatLevel > 0) StatusAmber.copy(alpha = 0.2f)
                    else DarkSurfaceVariant
                )
                .clickable(onClick = onCycleSeatHeat)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.HeatPump,
                    contentDescription = "Seat Heating",
                    tint = if (seatHeatLevel > 0) StatusAmber else TextMutedDark,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (seatHeatLevel > 0) "SEAT HEAT L$seatHeatLevel" else "SEAT HEAT OFF",
                    color = if (seatHeatLevel > 0) StatusAmber else TextMutedDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SystemToggleItem(
    title: String,
    isActive: Boolean,
    icon: ImageVector,
    activeColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    if (isActive) activeColor.copy(alpha = 0.25f)
                    else DarkSurfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isActive) activeColor else TextMutedDark,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = title,
            color = if (isActive) activeColor else TextMutedDark,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AuxControlButton(
    title: String,
    isActive: Boolean,
    icon: ImageVector,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isActive) activeColor.copy(alpha = 0.25f)
                else DarkSurfaceVariant
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isActive) activeColor else TextMutedDark,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                color = if (isActive) TextPrimaryDark else TextMutedDark,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
