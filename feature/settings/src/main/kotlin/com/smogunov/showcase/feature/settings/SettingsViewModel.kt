package com.smogunov.showcase.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smogunov.showcase.core.domain.repository.UserDataRepository
import com.smogunov.showcase.core.model.ThemeConfig
import com.smogunov.showcase.core.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SettingsUiState {
    data object Loading : SettingsUiState

    data class Success(val userData: UserData) : SettingsUiState
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = userDataRepository.userData
        .map<UserData, SettingsUiState>(SettingsUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = SettingsUiState.Loading,
        )

    fun onThemeChange(themeConfig: ThemeConfig) {
        viewModelScope.launch { userDataRepository.setThemeConfig(themeConfig) }
    }

    fun onDynamicColorChange(enabled: Boolean) {
        viewModelScope.launch { userDataRepository.setDynamicColor(enabled) }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
