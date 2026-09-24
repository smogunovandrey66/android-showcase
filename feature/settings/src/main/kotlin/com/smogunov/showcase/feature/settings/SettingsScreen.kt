package com.smogunov.showcase.feature.settings

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smogunov.showcase.core.designsystem.component.LoadingState
import com.smogunov.showcase.core.model.ThemeConfig
import com.smogunov.showcase.core.model.UserData

@Composable
internal fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsContent(
        uiState = uiState,
        onThemeChange = viewModel::onThemeChange,
        onDynamicColorChange = viewModel::onDynamicColorChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsContent(
    uiState: SettingsUiState,
    onThemeChange: (ThemeConfig) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.feature_settings_title)) }) },
    ) { padding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(padding)
        when (uiState) {
            SettingsUiState.Loading -> LoadingState(contentModifier)
            is SettingsUiState.Success -> SettingsBody(
                userData = uiState.userData,
                onThemeChange = onThemeChange,
                onDynamicColorChange = onDynamicColorChange,
                modifier = contentModifier.verticalScroll(rememberScrollState()),
            )
        }
    }
}

@Composable
private fun SettingsBody(
    userData: UserData,
    onThemeChange: (ThemeConfig) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.feature_settings_theme),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        Column(Modifier.selectableGroup()) {
            ThemeConfig.entries.forEach { theme ->
                val selected = userData.themeConfig == theme
                ListItem(
                    headlineContent = { Text(theme.label()) },
                    leadingContent = { RadioButton(selected = selected, onClick = null) },
                    modifier = Modifier.selectable(
                        selected = selected,
                        role = Role.RadioButton,
                        onClick = { onThemeChange(theme) },
                    ),
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ListItem(
                headlineContent = { Text(stringResource(R.string.feature_settings_dynamic_color)) },
                supportingContent = { Text(stringResource(R.string.feature_settings_dynamic_color_summary)) },
                trailingContent = { Switch(checked = userData.useDynamicColor, onCheckedChange = null) },
                modifier = Modifier.toggleable(
                    value = userData.useDynamicColor,
                    role = Role.Switch,
                    onValueChange = onDynamicColorChange,
                ),
            )
        }
    }
}

@Composable
private fun ThemeConfig.label(): String = stringResource(
    when (this) {
        ThemeConfig.FOLLOW_SYSTEM -> R.string.feature_settings_theme_system
        ThemeConfig.LIGHT -> R.string.feature_settings_theme_light
        ThemeConfig.DARK -> R.string.feature_settings_theme_dark
    },
)
