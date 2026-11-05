package com.autosphere.dashboard.presentation.navigation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autosphere.dashboard.domain.model.Destination
import com.autosphere.dashboard.domain.model.DestinationCategory
import com.autosphere.dashboard.presentation.components.GlassCard
import com.autosphere.dashboard.presentation.theme.DarkBackground
import com.autosphere.dashboard.presentation.theme.DarkSurfaceVariant
import com.autosphere.dashboard.presentation.theme.ElectricBlue
import com.autosphere.dashboard.presentation.theme.NeonCyan
import com.autosphere.dashboard.presentation.theme.PulsePurple
import com.autosphere.dashboard.presentation.theme.StatusGreen
import com.autosphere.dashboard.presentation.theme.TextMutedDark
import com.autosphere.dashboard.presentation.theme.TextPrimaryDark

@Composable
fun NavigationScreen(
    viewModel: NavigationViewModel,
    modifier: Modifier = Modifier
) {
    val navState by viewModel.navigationState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val infiniteTransition = rememberInfiniteTransition(label = "navPulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "navGlow"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Futuristic Stylized Vector Map Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Background Grid / Road Network
            val roadColor = Color(0xFF162036)
            val secondaryRoadColor = Color(0xFF0F172A)

            // Arterial Highway Grid Lines
            drawLine(
                color = roadColor,
                start = Offset(0f, h * 0.35f),
                end = Offset(w, h * 0.35f),
                strokeWidth = 14.dp.toPx()
            )

            drawLine(
                color = roadColor,
                start = Offset(0f, h * 0.68f),
                end = Offset(w, h * 0.68f),
                strokeWidth = 16.dp.toPx()
            )

            drawLine(
                color = roadColor,
                start = Offset(w * 0.3f, 0f),
                end = Offset(w * 0.3f, h),
                strokeWidth = 12.dp.toPx()
            )

            drawLine(
                color = roadColor,
                start = Offset(w * 0.72f, 0f),
                end = Offset(w * 0.72f, h),
                strokeWidth = 14.dp.toPx()
            )

            // Diagonal Expressway
            drawLine(
                color = roadColor,
                start = Offset(0f, h * 0.85f),
                end = Offset(w * 0.85f, 0f),
                strokeWidth = 20.dp.toPx()
            )

            // Active Navigation Route Polyline (Glowing Electric Blue / Cyan)
            if (navState.isNavigating) {
                val routePath = Path().apply {
                    moveTo(w * 0.45f, h * 0.78f) // Current Vehicle Position
                    lineTo(w * 0.45f, h * 0.52f)
                    cubicTo(
                        w * 0.45f, h * 0.42f,
                        w * 0.52f, h * 0.38f,
                        w * 0.60f, h * 0.38f
                    )
                    lineTo(w * 0.82f, h * 0.38f)
                    cubicTo(
                        w * 0.88f, h * 0.38f,
                        w * 0.90f, h * 0.32f,
                        w * 0.90f, h * 0.22f
                    )
                    lineTo(w * 0.90f, h * 0.15f) // Destination Point
                }

                // Outer route neon glow
                drawPath(
                    path = routePath,
                    color = NeonCyan.copy(alpha = 0.25f * pulseGlow),
                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                )

                // Main route line
                drawPath(
                    path = routePath,
                    brush = Brush.linearGradient(
                        listOf(ElectricBlue, NeonCyan)
                    ),
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )

                // Destination Pin Marker
                drawCircle(
                    color = PulsePurple,
                    center = Offset(w * 0.90f, h * 0.15f),
                    radius = 10.dp.toPx()
                )
                drawCircle(
                    color = Color.White,
                    center = Offset(w * 0.90f, h * 0.15f),
                    radius = 4.dp.toPx()
                )

                // Vehicle Position Pulsing Marker
                drawCircle(
                    color = ElectricBlue.copy(alpha = 0.3f * pulseGlow),
                    center = Offset(w * 0.45f, h * 0.78f),
                    radius = 24.dp.toPx()
                )
                drawCircle(
                    color = ElectricBlue,
                    center = Offset(w * 0.45f, h * 0.78f),
                    radius = 9.dp.toPx()
                )
                drawCircle(
                    color = Color.White,
                    center = Offset(w * 0.45f, h * 0.78f),
                    radius = 4.dp.toPx()
                )
            }
        }

        // Overlay UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Search Destination & Speed Limit Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Bar Glass Card
                GlassCard(
                    modifier = Modifier.width(420.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = ElectricBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = if (searchQuery.isEmpty()) "Search address, charger, or landmark..." else searchQuery,
                            color = if (searchQuery.isEmpty()) TextMutedDark else TextPrimaryDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Speed Limit Badge (European/AAOS Style Round Sign)
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${navState.speedLimitKmh}",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            // Middle Left: Active Turn-by-Turn Guidance Card
            if (navState.isNavigating) {
                GlassCard(
                    modifier = Modifier.width(360.dp),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NearMe,
                                    contentDescription = "Maneuver",
                                    tint = Color.Black,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "IN ${navState.distanceToManeuverMeters} METERS",
                                    color = ElectricBlue,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = navState.maneuverInstruction,
                                    color = TextPrimaryDark,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Route Progress Summary Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "ETA ${navState.etaString} (${navState.estimatedMinutesRemaining} min)",
                                    color = TextPrimaryDark,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "%.1f km remaining".format(navState.distanceRemainingKm),
                                    color = TextMutedDark,
                                    fontSize = 11.sp
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BatteryChargingFull,
                                    contentDescription = null,
                                    tint = StatusGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${navState.arrivalBatteryPercent}% ARRIVAL",
                                    color = StatusGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Row: Frequent / Suggested Destinations
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "FREQUENT & EV CHARGERS",
                    color = TextMutedDark,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(navState.savedDestinations) { destination ->
                        GlassCard(
                            shape = RoundedCornerShape(16.dp),
                            onClick = { viewModel.selectDestination(destination) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                val icon = when (destination.category) {
                                    DestinationCategory.HOME -> Icons.Default.Home
                                    DestinationCategory.WORK -> Icons.Default.Work
                                    DestinationCategory.SUPERCHARGER -> Icons.Default.EvStation
                                    else -> Icons.Default.Directions
                                }
                                val iconColor = when (destination.category) {
                                    DestinationCategory.SUPERCHARGER -> StatusGreen
                                    else -> ElectricBlue
                                }

                                Icon(
                                    imageVector = icon,
                                    contentDescription = destination.name,
                                    tint = iconColor,
                                    modifier = Modifier.size(20.dp)
                                )

                                Column {
                                    Text(
                                        text = destination.name,
                                        color = TextPrimaryDark,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "%.1f km • %d min".format(destination.distanceKm, destination.estimatedTimeMinutes),
                                        color = TextMutedDark,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
