package com.autosphere.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.SevereCold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autosphere.dashboard.domain.model.ClimateState
import com.autosphere.dashboard.presentation.theme.DarkSurfaceVariant
import com.autosphere.dashboard.presentation.theme.ElectricBlue
import com.autosphere.dashboard.presentation.theme.NeonCyan
import com.autosphere.dashboard.presentation.theme.PulsePurple
import com.autosphere.dashboard.presentation.theme.StatusAmber
import com.autosphere.dashboard.presentation.theme.TextMutedDark
import com.autosphere.dashboard.presentation.theme.TextPrimaryDark

@Composable
fun ClimateQuickWidget(
    climateState: ClimateState,
    useFahrenheit: Boolean = false,
    onAdjustDriverTemp: (Float) -> Unit,
    onAdjustPassengerTemp: (Float) -> Unit,
    onToggleAc: () -> Unit,
    onToggleAuto: () -> Unit,
    onToggleFrontDefrost: () -> Unit,
    onCycleDriverSeatHeat: () -> Unit,
    modifier: Modifier = Modifier
) {
    fun formatTemp(celsius: Float): String {
        return if (useFahrenheit) {
            "%.1f°F".format((celsius * 9f / 5f) + 32f)
        } else {
            "%.1f°C".format(celsius)
        }
    }

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CLIMATE CONTROL",
                    color = TextMutedDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ClimatePillButton(
                        text = "AC",
                        isActive = climateState.isAcOn,
                        activeColor = NeonCyan,
                        onClick = onToggleAc
                    )
                    ClimatePillButton(
                        text = "AUTO",
                        isActive = climateState.isAutoMode,
                        activeColor = ElectricBlue,
                        onClick = onToggleAuto
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Dual Zone Temperature Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Driver Side
                TempControlColumn(
                    label = "DRIVER",
                    tempString = formatTemp(climateState.driverTempC),
                    seatHeatLevel = climateState.driverSeatHeatingLevel,
                    onTempDown = { onAdjustDriverTemp(-0.5f) },
                    onTempUp = { onAdjustDriverTemp(0.5f) },
                    onSeatHeatClick = onCycleDriverSeatHeat
                )

                // Fan Speed Indicator in Center
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = "Fan Speed",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "FAN ${climateState.fanSpeed}",
                        color = TextMutedDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        for (i in 1..5) {
                            Box(
                                modifier = Modifier
                                    .size(width = 6.dp, height = 4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        if (i <= climateState.fanSpeed) NeonCyan else Color(0xFF334155)
                                    )
                            )
                        }
                    }
                }

                // Passenger Side
                TempControlColumn(
                    label = "PASSENGER",
                    tempString = formatTemp(climateState.passengerTempC),
                    seatHeatLevel = climateState.passengerSeatHeatingLevel,
                    onTempDown = { onAdjustPassengerTemp(-0.5f) },
                    onTempUp = { onAdjustPassengerTemp(0.5f) },
                    onSeatHeatClick = null
                )
            }

            Spacer(Modifier.height(14.dp))

            // Defrost Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickDefrostButton(
                    title = "FRONT DEFROST",
                    isActive = climateState.isFrontDefrostOn,
                    icon = Icons.Default.SevereCold,
                    onClick = onToggleFrontDefrost,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TempControlColumn(
    label: String,
    tempString: String,
    seatHeatLevel: Int,
    onTempDown: () -> Unit,
    onTempUp: () -> Unit,
    onSeatHeatClick: (() -> Unit)?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = TextMutedDark,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoundStepperButton(text = "−", onClick = onTempDown)

            Text(
                text = tempString,
                color = TextPrimaryDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            RoundStepperButton(text = "+", onClick = onTempUp)
        }

        if (onSeatHeatClick != null) {
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (seatHeatLevel > 0) StatusAmber.copy(alpha = 0.2f)
                        else DarkSurfaceVariant
                    )
                    .clickable(onClick = onSeatHeatClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HeatPump,
                        contentDescription = "Seat Heating",
                        tint = if (seatHeatLevel > 0) StatusAmber else TextMutedDark,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (seatHeatLevel > 0) "HEAT L$seatHeatLevel" else "HEAT OFF",
                        color = if (seatHeatLevel > 0) StatusAmber else TextMutedDark,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RoundStepperButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(DarkSurfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = TextPrimaryDark,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ClimatePillButton(
    text: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isActive) activeColor.copy(alpha = 0.2f) else DarkSurfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = if (isActive) activeColor else TextMutedDark,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun QuickDefrostButton(
    title: String,
    isActive: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isActive) ElectricBlue.copy(alpha = 0.25f)
                else DarkSurfaceVariant
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isActive) ElectricBlue else TextMutedDark,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = title,
                color = if (isActive) ElectricBlue else TextMutedDark,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
