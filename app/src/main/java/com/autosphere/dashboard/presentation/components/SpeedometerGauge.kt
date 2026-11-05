package com.autosphere.dashboard.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autosphere.dashboard.domain.model.DrivingMode
import com.autosphere.dashboard.presentation.theme.ElectricBlue
import com.autosphere.dashboard.presentation.theme.NeonCyan
import com.autosphere.dashboard.presentation.theme.PulsePurple
import com.autosphere.dashboard.presentation.theme.StatusGreen
import com.autosphere.dashboard.presentation.theme.StatusRed
import com.autosphere.dashboard.presentation.theme.TextMutedDark
import com.autosphere.dashboard.presentation.theme.TextPrimaryDark
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedometerGauge(
    speed: Float,
    maxSpeed: Float = 240f,
    useMph: Boolean = false,
    drivingMode: DrivingMode = DrivingMode.COMFORT,
    modifier: Modifier = Modifier,
    size: Dp = 260.dp
) {
    val displaySpeed = if (useMph) speed * 0.621371f else speed
    val effectiveMax = if (useMph) 150f else maxSpeed
    val unitLabel = if (useMph) "MPH" else "KM/H"

    val animatedSpeed by animateFloatAsState(
        targetValue = displaySpeed,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "animatedSpeed"
    )

    val progressFraction = (animatedSpeed / effectiveMax).coerceIn(0f, 1f)

    val (primaryAccent, secondaryAccent) = when (drivingMode) {
        DrivingMode.ECO -> StatusGreen to NeonCyan
        DrivingMode.COMFORT -> ElectricBlue to NeonCyan
        DrivingMode.SPORT -> StatusRed to PulsePurple
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()
            val startAngle = 135f
            val totalSweepAngle = 270f
            val activeSweepAngle = totalSweepAngle * progressFraction

            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = (this.size.minDimension - strokeWidth) / 2f

            // Background track arc
            drawArc(
                color = Color(0xFF1E293B).copy(alpha = 0.7f),
                startAngle = startAngle,
                sweepAngle = totalSweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Outer tick marks
            val tickCount = 24
            val tickRadius = radius + 10.dp.toPx()
            for (i in 0..tickCount) {
                val tickAngle = startAngle + (i.toFloat() / tickCount.toFloat()) * totalSweepAngle
                val rad = Math.toRadians(tickAngle.toDouble())
                val isMajor = i % 4 == 0
                val tickLength = if (isMajor) 10.dp.toPx() else 5.dp.toPx()
                val tickColor = if (tickAngle <= startAngle + activeSweepAngle) {
                    primaryAccent.copy(alpha = 0.9f)
                } else {
                    Color(0xFF334155)
                }

                val startX = (center.x + (tickRadius - tickLength) * cos(rad)).toFloat()
                val startY = (center.y + (tickRadius - tickLength) * sin(rad)).toFloat()
                val endX = (center.x + tickRadius * cos(rad)).toFloat()
                val endY = (center.y + tickRadius * sin(rad)).toFloat()

                drawLine(
                    color = tickColor,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (isMajor) 2.5.dp.toPx() else 1.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Active animated gradient arc
            val gradientBrush = Brush.sweepGradient(
                listOf(secondaryAccent, primaryAccent),
                center = center
            )

            if (activeSweepAngle > 0.5f) {
                drawArc(
                    brush = gradientBrush,
                    startAngle = startAngle,
                    sweepAngle = activeSweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Inner Digital Readout
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "SPEED",
                color = TextMutedDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Text(
                text = "${animatedSpeed.toInt()}",
                color = TextPrimaryDark,
                fontSize = 58.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1.5).sp
            )

            Text(
                text = unitLabel,
                color = primaryAccent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(4.dp))

            // Subdued mode label
            Text(
                text = drivingMode.title.uppercase(),
                color = secondaryAccent.copy(alpha = 0.8f),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }
    }
}
