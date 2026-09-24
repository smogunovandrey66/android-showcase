package com.smogunov.showcase.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight

private val Default = Typography()

internal val ShowcaseTypography = Typography(
    titleLarge = Default.titleLarge.copy(fontWeight = FontWeight.SemiBold),
    titleMedium = Default.titleMedium.copy(fontWeight = FontWeight.SemiBold),
)
