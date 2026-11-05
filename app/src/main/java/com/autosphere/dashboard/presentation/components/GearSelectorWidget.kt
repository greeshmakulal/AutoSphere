package com.autosphere.dashboard.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autosphere.dashboard.domain.model.Gear
import com.autosphere.dashboard.presentation.theme.DarkCardBackground
import com.autosphere.dashboard.presentation.theme.ElectricBlue
import com.autosphere.dashboard.presentation.theme.StatusAmber
import com.autosphere.dashboard.presentation.theme.StatusGreen
import com.autosphere.dashboard.presentation.theme.StatusRed
import com.autosphere.dashboard.presentation.theme.TextMutedDark
import com.autosphere.dashboard.presentation.theme.TextPrimaryDark

@Composable
fun GearSelectorWidget(
    currentGear: Gear,
    onGearSelected: (Gear) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(DarkCardBackground)
            .padding(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Gear.values().forEach { gear ->
                val isSelected = gear == currentGear
                val activeBgColor by animateColorAsState(
                    targetValue = if (isSelected) {
                        when (gear) {
                            Gear.PARK -> StatusRed.copy(alpha = 0.85f)
                            Gear.REVERSE -> StatusAmber.copy(alpha = 0.85f)
                            Gear.NEUTRAL -> Color(0xFF475569)
                            Gear.DRIVE -> ElectricBlue.copy(alpha = 0.9f)
                        }
                    } else Color.Transparent,
                    animationSpec = tween(durationMillis = 200),
                    label = "gearBg"
                )

                val textColor by animateColorAsState(
                    targetValue = if (isSelected) TextPrimaryDark else TextMutedDark,
                    label = "gearTextColor"
                )

                Box(
                    modifier = Modifier
                        .size(width = 48.dp, height = 44.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(activeBgColor)
                        .clickable { onGearSelected(gear) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = gear.display,
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
