package com.smogunov.showcase.core.domain.repository

import com.smogunov.showcase.core.model.ThemeConfig
import com.smogunov.showcase.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setThemeConfig(themeConfig: ThemeConfig)

    suspend fun setDynamicColor(useDynamicColor: Boolean)
}
