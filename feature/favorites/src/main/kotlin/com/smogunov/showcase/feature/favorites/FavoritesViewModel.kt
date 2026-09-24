package com.smogunov.showcase.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smogunov.showcase.core.domain.repository.FavoritesRepository
import com.smogunov.showcase.core.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState

    data object Empty : FavoritesUiState

    data class Success(val favorites: List<Character>) : FavoritesUiState
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoritesRepository: FavoritesRepository,
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = favoritesRepository.observeFavorites()
        .map { favorites ->
            if (favorites.isEmpty()) FavoritesUiState.Empty else FavoritesUiState.Success(favorites)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = FavoritesUiState.Loading,
        )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
