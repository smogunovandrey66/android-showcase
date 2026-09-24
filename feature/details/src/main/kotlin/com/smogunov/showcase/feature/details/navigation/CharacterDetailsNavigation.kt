package com.smogunov.showcase.feature.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.smogunov.showcase.feature.details.CharacterDetailsScreen
import kotlinx.serialization.Serializable

/** Type-safe route: arguments are regular constructor properties. */
@Serializable
data class CharacterDetailsRoute(val id: Int)

fun NavController.navigateToCharacterDetails(id: Int, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(CharacterDetailsRoute(id), navOptions)
}

fun NavGraphBuilder.characterDetailsScreen(onBackClick: () -> Unit) {
    composable<CharacterDetailsRoute> {
        CharacterDetailsScreen(onBackClick = onBackClick)
    }
}
