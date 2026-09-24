package com.smogunov.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smogunov.showcase.core.designsystem.theme.ShowcaseTheme
import com.smogunov.showcase.core.model.ThemeConfig
import com.smogunov.showcase.ui.ShowcaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen until user preferences (theme) are loaded to avoid a theme flash.
        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value is MainActivityUiState.Loading }
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ShowcaseTheme(
                darkTheme = shouldUseDarkTheme(uiState),
                dynamicColor = uiState.useDynamicColor(),
            ) {
                ShowcaseApp()
            }
        }
    }
}

@Composable
private fun shouldUseDarkTheme(uiState: MainActivityUiState): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.themeConfig) {
        ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        ThemeConfig.LIGHT -> false
        ThemeConfig.DARK -> true
    }
}

private fun MainActivityUiState.useDynamicColor(): Boolean = when (this) {
    MainActivityUiState.Loading -> true
    is MainActivityUiState.Success -> userData.useDynamicColor
}
