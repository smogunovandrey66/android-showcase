package com.smogunov.showcase.feature.settings

import com.smogunov.showcase.core.model.ThemeConfig
import com.smogunov.showcase.core.testing.repository.FakeUserDataRepository
import com.smogunov.showcase.core.testing.repository.defaultUserData
import com.smogunov.showcase.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeUserDataRepository()

    @Test
    fun stateReflectsUserChanges() = runTest {
        val viewModel = SettingsViewModel(repository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
        assertEquals(SettingsUiState.Success(defaultUserData), viewModel.uiState.value)

        viewModel.onThemeChange(ThemeConfig.DARK)
        viewModel.onDynamicColorChange(false)

        assertEquals(
            SettingsUiState.Success(defaultUserData.copy(themeConfig = ThemeConfig.DARK, useDynamicColor = false)),
            viewModel.uiState.value,
        )
    }
}
