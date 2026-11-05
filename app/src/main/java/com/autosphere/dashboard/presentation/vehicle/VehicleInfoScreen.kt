package com.autosphere.dashboard.presentation.vehicle

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TireRepair
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autosphere.dashboard.presentation.components.GlassCard
import com.autosphere.dashboard.presentation.components.VehicleIllustration
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
fun VehicleInfoScreen(
    viewModel: VehicleViewModel,
    modifier: Modifier = Modifier
) {
    val vehicleState by viewModel.vehicleState.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()

    val odoDisplay = if (userSettings.useMph) {
        "%.1f mi".format(vehicleState.odometerKm * 0.621371f)
    } else {
        "%.1f km".format(vehicleState.odometerKm)
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
                text = "VEHICLE TELEMETRY & HEALTH",
                color = TextPrimaryDark,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (vehicleState.isCharging) NeonCyan.copy(alpha = 0.2f)
                            else DarkSurfaceVariant
                        )
                        .clickable { viewModel.toggleCharging() }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Charge Port",
                            tint = if (vehicleState.isCharging) NeonCyan else TextMutedDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (vehicleState.isCharging) "CHARGING DC 150 kW" else "PLUG IN / CHARGE",
                            color = if (vehicleState.isCharging) NeonCyan else TextMutedDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Split Layout: Left TPMS & Center Vehicle + Right Subsystem Telemetry
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Left Card: 4-Wheel Tire Pressure Monitoring System (TPMS)
            GlassCard(
                modifier = Modifier
                    .weight(1.1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TIRE PRESSURE MONITORING (TPMS)",
                            color = TextMutedDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = if (vehicleState.tirePressure.isAllNormal) "ALL NORMAL" else "CHECK TIRES",
                            color = if (vehicleState.tirePressure.isAllNormal) StatusGreen else StatusAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Vehicle with 4 Tire Callouts
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        VehicleIllustration(
                            isHeadlightsOn = vehicleState.isHeadlightsOn,
                            isDoorsLocked = vehicleState.isDoorsLocked,
                            isCharging = vehicleState.isCharging,
                            size = 190.dp
                        )

                        // Front Left
                        TirePressureBadge(
                            label = "FL",
                            psi = vehicleState.tirePressure.frontLeftPsi,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(top = 16.dp, start = 8.dp)
                        )

                        // Front Right
                        TirePressureBadge(
                            label = "FR",
                            psi = vehicleState.tirePressure.frontRightPsi,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 16.dp, end = 8.dp)
                        )

                        // Rear Left
                        TirePressureBadge(
                            label = "RL",
                            psi = vehicleState.tirePressure.rearLeftPsi,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(bottom = 16.dp, start = 8.dp)
                        )

                        // Rear Right
                        TirePressureBadge(
                            label = "RR",
                            psi = vehicleState.tirePressure.rearRightPsi,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 16.dp, end = 8.dp)
                        )
                    }

                    Text(
                        text = "TARGET COLD PRESSURE: 35.0 PSI (2.41 BAR)",
                        color = TextMutedDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Right Card: Diagnostics, Battery Pack & Powertrain Telemetry
            GlassCard(
                modifier = Modifier
                    .weight(1.1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ELECTRIC POWERTRAIN DIAGNOSTICS",
                        color = TextMutedDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    // Grid of 4 Subsystems
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricBlock(
                                title = "MOTOR TEMP",
                                value = "%.1f°C".format(vehicleState.motorTempC),
                                subtitle = "DUAL INVERTER OPTIMAL",
                                icon = Icons.Default.Thermostat,
                                tint = ElectricBlue,
                                modifier = Modifier.weight(1f)
                            )
                            MetricBlock(
                                title = "BATTERY VOLTAGE",
                                value = "%.1f V".format(vehicleState.batteryVoltage),
                                subtitle = "400V ARCHITECTURE",
                                icon = Icons.Default.Bolt,
                                tint = NeonCyan,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricBlock(
                                title = "ODOMETER",
                                value = odoDisplay,
                                subtitle = "LIFETIME TRAVEL",
                                icon = Icons.Default.Speed,
                                tint = PulsePurple,
                                modifier = Modifier.weight(1f)
                            )
                            MetricBlock(
                                title = "BATTERY HEALTH",
                                value = "98.4%",
                                subtitle = "STATE OF HEALTH (SOH)",
                                icon = Icons.Default.CheckCircle,
                                tint = StatusGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Energy Efficiency Summary
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurfaceVariant)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "RECENT CONSUMPTION",
                                    color = TextMutedDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${vehicleState.energyConsumptionWhPerKm} Wh/km",
                                    color = TextPrimaryDark,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "AAOS CAR SERVICE",
                                    color = TextMutedDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (vehicleState.isConnectedToCarService) "ONLINE (HAL V3)" else "EMULATED HAL",
                                    color = if (vehicleState.isConnectedToCarService) StatusGreen else ElectricBlue,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TirePressureBadge(
    label: String,
    psi: Float,
    modifier: Modifier = Modifier
) {
    val isNormal = psi in 32.0f..38.0f
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceVariant)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = TextMutedDark,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "%.1f".format(psi),
                color = if (isNormal) TextPrimaryDark else StatusAmber,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "PSI",
                color = if (isNormal) ElectricBlue else StatusAmber,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun MetricBlock(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurfaceVariant)
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = TextMutedDark,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = value,
                color = TextPrimaryDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = tint.copy(alpha = 0.8f),
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
