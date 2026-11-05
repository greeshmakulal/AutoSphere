package com.autosphere.dashboard.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autosphere.dashboard.presentation.theme.DarkCardBackground
import com.autosphere.dashboard.presentation.theme.DarkSurface
import com.autosphere.dashboard.presentation.theme.ElectricBlue
import com.autosphere.dashboard.presentation.theme.NeonCyan
import com.autosphere.dashboard.presentation.theme.StatusRed
import com.autosphere.dashboard.presentation.theme.TextMutedDark
import com.autosphere.dashboard.presentation.theme.TextPrimaryDark

enum class AutomotiveNavDestination(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    HOME("home", "Home", Icons.Default.Home),
    MEDIA("media", "Media", Icons.Default.MusicNote),
    CLIMATE("climate", "Climate", Icons.Default.AcUnit),
    VEHICLE("vehicle", "Vehicle", Icons.Default.DirectionsCar),
    NAVIGATION("navigation", "Nav", Icons.Default.Navigation),
    MODES("modes", "Modes", Icons.Default.Speed),
    SETTINGS("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun AutomotiveNavRail(
    currentRoute: String,
    onNavigate: (AutomotiveNavDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(92.dp)
            .fillMaxHeight()
            .background(DarkSurface)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // AutoSphere Brand Icon
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(DarkCardBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚡",
                fontSize = 24.sp
            )
        }

        Spacer(Modifier.height(8.dp))

        // Navigation Destination Buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            AutomotiveNavDestination.values().forEach { destination ->
                val isSelected = currentRoute == destination.route

                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) ElectricBlue else TextMutedDark,
                    animationSpec = tween(200),
                    label = "navIconColor"
                )

                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) ElectricBlue.copy(alpha = 0.16f) else Color.Transparent,
                    animationSpec = tween(200),
                    label = "navBgColor"
                )

                Box(
                    modifier = Modifier
                        .size(width = 76.dp, height = 58.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(backgroundColor)
                        .clickable { onNavigate(destination) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.title,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = destination.title,
                            color = iconColor,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
