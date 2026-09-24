package com.smogunov.showcase.core.domain.usecase

import app.cash.turbine.test
import com.smogunov.showcase.core.model.CharacterDetails
import com.smogunov.showcase.core.testing.data.testCharacter
import com.smogunov.showcase.core.testing.repository.FakeCharacterRepository
import com.smogunov.showcase.core.testing.repository.FakeFavoritesRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ObserveCharacterDetailsUseCaseTest {

    private val characterRepository = FakeCharacterRepository()
    private val favoritesRepository = FakeFavoritesRepository()
    private val useCase = ObserveCharacterDetailsUseCase(characterRepository, favoritesRepository)
    private val toggleFavorite = ToggleFavoriteUseCase(favoritesRepository)

    @Test
    fun emitsDetailsAndReactsToFavoriteChanges() = runTest {
        val character = testCharacter(id = 1)
        characterRepository.sendCharacters(listOf(character))

        useCase(id = 1).test {
            assertEquals(CharacterDetails(character, isFavorite = false), awaitItem())

            toggleFavorite(character, isFavorite = false)
            assertEquals(CharacterDetails(character, isFavorite = true), awaitItem())

            toggleFavorite(character, isFavorite = true)
            assertEquals(CharacterDetails(character, isFavorite = false), awaitItem())
        }
    }
}
