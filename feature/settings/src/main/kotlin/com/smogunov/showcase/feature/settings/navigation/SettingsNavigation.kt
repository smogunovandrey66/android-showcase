package com.smogunov.showcase.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smogunov.showcase.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute

fun NavGraphBuilder.settingsScreen() {
    composable<SettingsRoute> {
        SettingsScreen()
    }
}
