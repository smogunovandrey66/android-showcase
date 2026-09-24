package com.smogunov.showcase.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.smogunov.showcase.core.domain.usecase.ObserveCharacterDetailsUseCase
import com.smogunov.showcase.core.domain.usecase.ToggleFavoriteUseCase
import com.smogunov.showcase.core.testing.data.testCharacter
import com.smogunov.showcase.core.testing.repository.FakeCharacterRepository
import com.smogunov.showcase.core.testing.repository.FakeFavoritesRepository
import com.smogunov.showcase.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals

// Robolectric runner: SavedStateHandle.toRoute() relies on android.os.Bundle.
@RunWith(AndroidJUnit4::class)
class CharacterDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val characterRepository = FakeCharacterRepository()
    private val favoritesRepository = FakeFavoritesRepository()

    private fun createViewModel(id: Int) = CharacterDetailsViewModel(
        savedStateHandle = SavedStateHandle(mapOf("id" to id)),
        observeCharacterDetails = ObserveCharacterDetailsUseCase(characterRepository, favoritesRepository),
        toggleFavorite = ToggleFavoriteUseCase(favoritesRepository),
    )

    @Test
    fun loadsCharacterAndTogglesFavorite() = runTest {
        val character = testCharacter(id = 7)
        characterRepository.sendCharacters(listOf(character))
        val viewModel = createViewModel(id = 7)

        viewModel.uiState.test {
            val loaded = awaitSuccess()
            assertEquals(character, loaded.details.character)
            assertEquals(false, loaded.details.isFavorite)

            viewModel.onFavoriteClick()

            assertEquals(true, awaitSuccess().details.isFavorite)
        }
    }

    @Test
    fun errorStateAndRetry() = runTest {
        characterRepository.error = IOException("offline")
        val viewModel = createViewModel(id = 1)

        viewModel.uiState.test {
            var item = awaitItem()
            while (item == CharacterDetailsUiState.Loading) item = awaitItem()
            assertEquals(CharacterDetailsUiState.Error, item)

            characterRepository.error = null
            characterRepository.sendCharacters(listOf(testCharacter(id = 1)))
            viewModel.onRetry()

            assertEquals(1, awaitSuccess().details.character.id)
        }
    }

    /** Skips transient Loading/intermediate emissions (StateFlow may conflate them). */
    private suspend fun ReceiveTurbine<CharacterDetailsUiState>.awaitSuccess(): CharacterDetailsUiState.Success {
        while (true) {
            val item = awaitItem()
            if (item is CharacterDetailsUiState.Success) return item
        }
    }
}
