package com.smogunov.showcase.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.smogunov.showcase.R
import com.smogunov.showcase.feature.characters.navigation.CharactersRoute
import com.smogunov.showcase.feature.favorites.navigation.FavoritesRoute
import com.smogunov.showcase.feature.settings.navigation.SettingsRoute

/** Destinations shown in the bottom navigation bar. */
enum class TopLevelDestination(
    val route: Any,
    val icon: ImageVector,
    @StringRes val labelRes: Int,
) {
    CHARACTERS(route = CharactersRoute, icon = Icons.AutoMirrored.Filled.List, labelRes = R.string.nav_characters),
    FAVORITES(route = FavoritesRoute, icon = Icons.Filled.Favorite, labelRes = R.string.nav_favorites),
    SETTINGS(route = SettingsRoute, icon = Icons.Filled.Settings, labelRes = R.string.nav_settings),
}
