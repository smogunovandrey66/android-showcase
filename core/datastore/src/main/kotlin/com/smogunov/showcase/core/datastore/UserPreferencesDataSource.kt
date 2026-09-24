package com.smogunov.showcase.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.smogunov.showcase.core.model.ThemeConfig
import com.smogunov.showcase.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val userData: Flow<UserData> = dataStore.data.map { preferences ->
        UserData(
            themeConfig = preferences[Keys.THEME_CONFIG]
                ?.let { stored -> ThemeConfig.entries.firstOrNull { it.name == stored } }
                ?: ThemeConfig.FOLLOW_SYSTEM,
            useDynamicColor = preferences[Keys.USE_DYNAMIC_COLOR] ?: true,
        )
    }

    suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        dataStore.edit { it[Keys.THEME_CONFIG] = themeConfig.name }
    }

    suspend fun setDynamicColor(useDynamicColor: Boolean) {
        dataStore.edit { it[Keys.USE_DYNAMIC_COLOR] = useDynamicColor }
    }

    private object Keys {
        val THEME_CONFIG = stringPreferencesKey("theme_config")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
    }
}
