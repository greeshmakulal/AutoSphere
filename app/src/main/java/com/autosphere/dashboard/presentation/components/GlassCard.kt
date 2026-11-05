package com.autosphere.dashboard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.autosphere.dashboard.presentation.theme.DarkCardBackground
import com.autosphere.dashboard.presentation.theme.GlassBorderGradient

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f),
    borderColor: Color? = null,
    borderWidth: Dp = 1.dp,
    borderBrush: Brush? = GlassBorderGradient,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else Modifier

    val borderModifier = when {
        borderBrush != null -> Modifier.border(borderWidth, borderBrush, shape)
        borderColor != null -> Modifier.border(borderWidth, borderColor, shape)
        else -> Modifier
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape)
            .then(borderModifier)
            .then(clickableModifier)
    ) {
        content()
    }
}
