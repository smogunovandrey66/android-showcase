package com.smogunov.showcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.smogunov.showcase.feature.characters.navigation.CharactersRoute
import com.smogunov.showcase.feature.characters.navigation.charactersScreen
import com.smogunov.showcase.feature.details.navigation.characterDetailsScreen
import com.smogunov.showcase.feature.details.navigation.navigateToCharacterDetails
import com.smogunov.showcase.feature.favorites.navigation.favoritesScreen
import com.smogunov.showcase.feature.settings.navigation.settingsScreen

/** Features know nothing about each other; the app module wires their navigation together. */
@Composable
fun ShowcaseNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = CharactersRoute,
        modifier = modifier,
    ) {
        charactersScreen(onCharacterClick = navController::navigateToCharacterDetails)
        favoritesScreen(onCharacterClick = navController::navigateToCharacterDetails)
        settingsScreen()
        characterDetailsScreen(onBackClick = navController::popBackStack)
    }
}
