package com.smogunov.showcase.feature.favorites

import com.smogunov.showcase.core.testing.data.testCharacter
import com.smogunov.showcase.core.testing.repository.FakeFavoritesRepository
import com.smogunov.showcase.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class FavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeFavoritesRepository()

    @Test
    fun initialStateIsLoading() {
        val viewModel = FavoritesViewModel(repository)

        assertEquals(FavoritesUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun emptyThenSuccessWhenFavoriteIsAdded() = runTest {
        val viewModel = FavoritesViewModel(repository)

        // stateIn(WhileSubscribed) needs an active collector to start the upstream flow.
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        assertEquals(FavoritesUiState.Empty, viewModel.uiState.value)

        val character = testCharacter(id = 3)
        repository.addFavorite(character)

        assertEquals(FavoritesUiState.Success(listOf(character)), viewModel.uiState.value)
    }
}
