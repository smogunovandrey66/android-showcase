package com.smogunov.showcase.feature.characters.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smogunov.showcase.feature.characters.CharactersScreen
import kotlinx.serialization.Serializable

@Serializable
data object CharactersRoute

fun NavGraphBuilder.charactersScreen(onCharacterClick: (Int) -> Unit) {
    composable<CharactersRoute> {
        CharactersScreen(onCharacterClick = onCharacterClick)
    }
}
