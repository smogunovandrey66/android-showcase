package com.smogunov.showcase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smogunov.showcase.core.domain.repository.UserDataRepository
import com.smogunov.showcase.core.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(val userData: UserData) : MainActivityUiState
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData
        .map<UserData, MainActivityUiState>(MainActivityUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = MainActivityUiState.Loading,
        )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
