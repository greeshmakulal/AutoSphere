package com.autosphere.dashboard.presentation.drivingmodes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autosphere.dashboard.domain.model.DrivingMode
import com.autosphere.dashboard.presentation.components.GlassCard
import com.autosphere.dashboard.presentation.theme.DarkBackground
import com.autosphere.dashboard.presentation.theme.DarkSurfaceVariant
import com.autosphere.dashboard.presentation.theme.EcoModeGradient
import com.autosphere.dashboard.presentation.theme.ElectricBlue
import com.autosphere.dashboard.presentation.theme.NeonCyan
import com.autosphere.dashboard.presentation.theme.PulsePurple
import com.autosphere.dashboard.presentation.theme.SportModeGradient
import com.autosphere.dashboard.presentation.theme.StatusGreen
import com.autosphere.dashboard.presentation.theme.StatusRed
import com.autosphere.dashboard.presentation.theme.TextMutedDark
import com.autosphere.dashboard.presentation.theme.TextPrimaryDark

@Composable
fun DrivingModesScreen(
    viewModel: DrivingModesViewModel,
    modifier: Modifier = Modifier
) {
    val vehicleState by viewModel.vehicleState.collectAsState()

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
            Column {
                Text(
                    text = "DYNAMIC DRIVING MODES",
                    color = TextPrimaryDark,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Configures torque vectoring, suspension dampening, and regenerative braking",
                    color = TextMutedDark,
                    fontSize = 12.sp
                )
            }

            // Current Mode Indicator
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "CURRENT: ${vehicleState.drivingMode.title.uppercase()}",
                    color = when (vehicleState.drivingMode) {
                        DrivingMode.ECO -> StatusGreen
                        DrivingMode.COMFORT -> ElectricBlue
                        DrivingMode.SPORT -> StatusRed
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Three Mode Cards
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            DrivingMode.values().forEach { mode ->
                val isSelected = vehicleState.drivingMode == mode

                val (accentColor, modeIcon) = when (mode) {
                    DrivingMode.ECO -> StatusGreen to Icons.Default.EnergySavingsLeaf
                    DrivingMode.COMFORT -> ElectricBlue to Icons.Default.ElectricCar
                    DrivingMode.SPORT -> StatusRed to Icons.Default.Speed
                }

                val borderModifier = if (isSelected) {
                    Modifier.border(
                        2.dp,
                        Brush.linearGradient(listOf(accentColor, PulsePurple)),
                        RoundedCornerShape(24.dp)
                    )
                } else Modifier

                GlassCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .then(borderModifier),
                    shape = RoundedCornerShape(24.dp),
                    onClick = { viewModel.selectDrivingMode(mode) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(22.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Card Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(accentColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = modeIcon,
                                    contentDescription = mode.title,
                                    tint = accentColor,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(accentColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Active",
                                        tint = Color.Black,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // Title & Description
                        Column {
                            Text(
                                text = mode.title,
                                color = TextPrimaryDark,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = mode.description,
                                color = TextMutedDark,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }

                        // Vehicle Tuning Attributes
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            ModeAttributeRow("THROTTLE", mode.throttleResponse, accentColor)
                            ModeAttributeRow("SUSPENSION", mode.suspension, accentColor)
                            ModeAttributeRow("REGEN BRAKE", mode.regenBraking, accentColor)
                            ModeAttributeRow("STEERING", mode.steeringFeel, accentColor)
                        }

                        // Efficiency Rating
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "EFFICIENCY RATING",
                                color = TextMutedDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${mode.efficiencyScore} / 100",
                                color = accentColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModeAttributeRow(
    label: String,
    value: String,
    accentColor: Color
) {
    Column {
        Text(
            text = label,
            color = TextMutedDark,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Text(
            text = value,
            color = TextPrimaryDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
