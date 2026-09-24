package com.smogunov.showcase.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.smogunov.showcase.core.designsystem.theme.LocalExtendedColors
import com.smogunov.showcase.core.model.CharacterStatus

@Composable
fun CharacterStatus.label(): String = stringResource(
    when (this) {
        CharacterStatus.ALIVE -> R.string.core_ui_status_alive
        CharacterStatus.DEAD -> R.string.core_ui_status_dead
        CharacterStatus.UNKNOWN -> R.string.core_ui_status_unknown
    },
)

@Composable
fun CharacterStatus.color(): Color {
    val colors = LocalExtendedColors.current
    return when (this) {
        CharacterStatus.ALIVE -> colors.statusAlive
        CharacterStatus.DEAD -> colors.statusDead
        CharacterStatus.UNKNOWN -> colors.statusUnknown
    }
}
