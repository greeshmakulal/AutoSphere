package com.autosphere.dashboard.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// AutoSphere Dark Theme Palette
val DarkBackground = Color(0xFF070B12)
val DarkSurface = Color(0xFF0F172A)
val DarkSurfaceVariant = Color(0xFF162036)
val DarkCardBackground = Color(0x331E293B) // Frosted glass effect
val DarkCardBorder = Color(0x3338BDF8)

// Accent Colors
val ElectricBlue = Color(0xFF38BDF8)
val NeonCyan = Color(0xFF06B6D4)
val PulsePurple = Color(0xFFA855F7)
val GlowViolet = Color(0xFF818CF8)

// Status Indicators
val StatusGreen = Color(0xFF10B981)
val StatusAmber = Color(0xFFF59E0B)
val StatusRed = Color(0xFFEF4444)

// Text Colors
val TextPrimaryDark = Color(0xFFF8FAFC)
val TextSecondaryDark = Color(0xFF94A3B8)
val TextMutedDark = Color(0xFF64748B)

// Light Theme Palette (High-contrast automotive daylight mode)
val LightBackground = Color(0xFFF1F5F9)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFE2E8F0)
val LightCardBackground = Color(0xCCFFFFFF)
val LightCardBorder = Color(0x330284C7)
val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF475569)
val TextMutedLight = Color(0xFF94A3B8)

// Gradients
val GlassBorderGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x5538BDF8),
        Color(0x15FFFFFF),
        Color(0x33A855F7)
    )
)

val SpeedometerGlowGradient = Brush.radialGradient(
    colors = listOf(
        Color(0x4038BDF8),
        Color(0x1006B6D4),
        Color(0x00000000)
    )
)

val BatteryChargedGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF10B981),
        Color(0xFF34D399)
    )
)

val SportModeGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFEF4444),
        Color(0xFFA855F7)
    )
)

val EcoModeGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF10B981),
        Color(0xFF06B6D4)
    )
)
