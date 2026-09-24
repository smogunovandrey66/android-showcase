package com.smogunov.showcase.feature.favorites.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smogunov.showcase.feature.favorites.FavoritesScreen
import kotlinx.serialization.Serializable

@Serializable
data object FavoritesRoute

fun NavGraphBuilder.favoritesScreen(onCharacterClick: (Int) -> Unit) {
    composable<FavoritesRoute> {
        FavoritesScreen(onCharacterClick = onCharacterClick)
    }
}
