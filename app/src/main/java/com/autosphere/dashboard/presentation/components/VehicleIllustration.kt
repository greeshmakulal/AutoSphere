package com.autosphere.dashboard.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.autosphere.dashboard.presentation.theme.ElectricBlue
import com.autosphere.dashboard.presentation.theme.NeonCyan
import com.autosphere.dashboard.presentation.theme.PulsePurple
import com.autosphere.dashboard.presentation.theme.StatusGreen
import com.autosphere.dashboard.presentation.theme.StatusRed

@Composable
fun VehicleIllustration(
    isHeadlightsOn: Boolean = true,
    isDoorsLocked: Boolean = true,
    isCharging: Boolean = false,
    modifier: Modifier = Modifier,
    size: Dp = 220.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            val centerX = w / 2f
            val centerY = h / 2f

            // Headlight beams projection (forward illumination glow)
            if (isHeadlightsOn) {
                val headlightBeamPath = Path().apply {
                    moveTo(centerX - 35.dp.toPx(), centerY - 65.dp.toPx())
                    lineTo(centerX - 95.dp.toPx(), centerY - 150.dp.toPx())
                    lineTo(centerX + 95.dp.toPx(), centerY - 150.dp.toPx())
                    lineTo(centerX + 35.dp.toPx(), centerY - 65.dp.toPx())
                    close()
                }

                drawPath(
                    path = headlightBeamPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            NeonCyan.copy(alpha = 0.18f * pulseGlow)
                        ),
                        startY = centerY - 150.dp.toPx(),
                        endY = centerY - 65.dp.toPx()
                    )
                )
            }

            // Charging halo ring if plugged in
            if (isCharging) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            StatusGreen.copy(alpha = 0.35f * pulseGlow),
                            Color.Transparent
                        ),
                        center = Offset(centerX, centerY),
                        radius = 80.dp.toPx()
                    )
                )
            }

            // Vehicle Body Silhouette (Top-down luxury EV shape)
            val bodyWidth = 72.dp.toPx()
            val bodyHeight = 145.dp.toPx()

            // Wheels (Front Left, Front Right, Rear Left, Rear Right)
            val wheelW = 10.dp.toPx()
            val wheelH = 26.dp.toPx()
            val wheelColor = Color(0xFF334155)

            // FL wheel
            drawRoundRect(
                color = wheelColor,
                topLeft = Offset(centerX - bodyWidth / 2f - wheelW + 2.dp.toPx(), centerY - bodyHeight / 2f + 16.dp.toPx()),
                size = Size(wheelW, wheelH),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
            // FR wheel
            drawRoundRect(
                color = wheelColor,
                topLeft = Offset(centerX + bodyWidth / 2f - 2.dp.toPx(), centerY - bodyHeight / 2f + 16.dp.toPx()),
                size = Size(wheelW, wheelH),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
            // RL wheel
            drawRoundRect(
                color = wheelColor,
                topLeft = Offset(centerX - bodyWidth / 2f - wheelW + 2.dp.toPx(), centerY + bodyHeight / 2f - 42.dp.toPx()),
                size = Size(wheelW, wheelH),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
            // RR wheel
            drawRoundRect(
                color = wheelColor,
                topLeft = Offset(centerX + bodyWidth / 2f - 2.dp.toPx(), centerY + bodyHeight / 2f - 42.dp.toPx()),
                size = Size(wheelW, wheelH),
                cornerRadius = CornerRadius(4.dp.toPx())
            )

            // Main Chassis
            val chassisPath = Path().apply {
                // Nose
                moveTo(centerX, centerY - bodyHeight / 2f)
                // Front Right Fender
                cubicTo(
                    centerX + bodyWidth / 2f - 6.dp.toPx(), centerY - bodyHeight / 2f + 4.dp.toPx(),
                    centerX + bodyWidth / 2f, centerY - bodyHeight / 2f + 25.dp.toPx(),
                    centerX + bodyWidth / 2f, centerY - bodyHeight / 2f + 45.dp.toPx()
                )
                // Right Door & Side Skirt
                lineTo(centerX + bodyWidth / 2f - 2.dp.toPx(), centerY + bodyHeight / 2f - 40.dp.toPx())
                // Rear Right Haunch
                cubicTo(
                    centerX + bodyWidth / 2f + 2.dp.toPx(), centerY + bodyHeight / 2f - 20.dp.toPx(),
                    centerX + bodyWidth / 2f - 4.dp.toPx(), centerY + bodyHeight / 2f,
                    centerX, centerY + bodyHeight / 2f
                )
                // Rear Left Haunch
                cubicTo(
                    centerX - bodyWidth / 2f + 4.dp.toPx(), centerY + bodyHeight / 2f,
                    centerX - bodyWidth / 2f - 2.dp.toPx(), centerY + bodyHeight / 2f - 20.dp.toPx(),
                    centerX - bodyWidth / 2f + 2.dp.toPx(), centerY + bodyHeight / 2f - 40.dp.toPx()
                )
                // Left Door & Side Skirt
                lineTo(centerX - bodyWidth / 2f, centerY - bodyHeight / 2f + 45.dp.toPx())
                // Front Left Fender
                cubicTo(
                    centerX - bodyWidth / 2f, centerY - bodyHeight / 2f + 25.dp.toPx(),
                    centerX - bodyWidth / 2f + 6.dp.toPx(), centerY - bodyHeight / 2f + 4.dp.toPx(),
                    centerX, centerY - bodyHeight / 2f
                )
                close()
            }

            // Chassis gradient fill
            drawPath(
                path = chassisPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E293B),
                        Color(0xFF0F172A),
                        Color(0xFF162036)
                    ),
                    startY = centerY - bodyHeight / 2f,
                    endY = centerY + bodyHeight / 2f
                )
            )

            // Chassis outer contour stroke
            drawPath(
                path = chassisPath,
                brush = Brush.linearGradient(
                    colors = listOf(ElectricBlue, NeonCyan, PulsePurple)
                ),
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )

            // Panoramic Glass Roof
            val glassRoofPath = Path().apply {
                val glassW = bodyWidth * 0.65f
                val glassH = bodyHeight * 0.48f
                addRoundRect(
                    RoundRect(
                        rect = androidx.compose.ui.geometry.Rect(
                            offset = Offset(centerX - glassW / 2f, centerY - glassH / 2f + 5.dp.toPx()),
                            size = Size(glassW, glassH)
                        ),
                        topLeft = CornerRadius(14.dp.toPx()),
                        topRight = CornerRadius(14.dp.toPx()),
                        bottomLeft = CornerRadius(10.dp.toPx()),
                        bottomRight = CornerRadius(10.dp.toPx())
                    )
                )
            }

            drawPath(
                path = glassRoofPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0F1D),
                        Color(0xFF14243B)
                    )
                )
            )

            drawPath(
                path = glassRoofPath,
                color = ElectricBlue.copy(alpha = 0.4f),
                style = Stroke(width = 1.dp.toPx())
            )

            // Front LED Lightbar
            if (isHeadlightsOn) {
                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(NeonCyan, Color.White, NeonCyan)
                    ),
                    start = Offset(centerX - bodyWidth / 2f + 10.dp.toPx(), centerY - bodyHeight / 2f + 3.dp.toPx()),
                    end = Offset(centerX + bodyWidth / 2f - 10.dp.toPx(), centerY - bodyHeight / 2f + 3.dp.toPx()),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Rear Neon Tail Light Strip
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(StatusRed, Color(0xFFFF4D4D), StatusRed)
                ),
                start = Offset(centerX - bodyWidth / 2f + 8.dp.toPx(), centerY + bodyHeight / 2f - 3.dp.toPx()),
                end = Offset(centerX + bodyWidth / 2f - 8.dp.toPx(), centerY + bodyHeight / 2f - 3.dp.toPx()),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}
