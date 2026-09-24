package com.smogunov.showcase.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.smogunov.showcase.core.common.result.Result
import com.smogunov.showcase.core.common.result.asResult
import com.smogunov.showcase.core.domain.usecase.ObserveCharacterDetailsUseCase
import com.smogunov.showcase.core.domain.usecase.ToggleFavoriteUseCase
import com.smogunov.showcase.core.model.CharacterDetails
import com.smogunov.showcase.feature.details.navigation.CharacterDetailsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CharacterDetailsUiState {
    data object Loading : CharacterDetailsUiState

    data object Error : CharacterDetailsUiState

    data class Success(val details: CharacterDetails) : CharacterDetailsUiState
}

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeCharacterDetails: ObserveCharacterDetailsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val characterId = savedStateHandle.toRoute<CharacterDetailsRoute>().id

    /** Incrementing this re-subscribes to the source flow, i.e. "retry". */
    private val retryTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CharacterDetailsUiState> = retryTrigger
        .flatMapLatest { observeCharacterDetails(characterId).asResult() }
        .map { result ->
            when (result) {
                is Result.Success -> CharacterDetailsUiState.Success(result.data)
                is Result.Error -> CharacterDetailsUiState.Error
                Result.Loading -> CharacterDetailsUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = CharacterDetailsUiState.Loading,
        )

    fun onFavoriteClick() {
        val state = uiState.value as? CharacterDetailsUiState.Success ?: return
        viewModelScope.launch {
            toggleFavorite(state.details.character, state.details.isFavorite)
        }
    }

    fun onRetry() {
        retryTrigger.value++
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
