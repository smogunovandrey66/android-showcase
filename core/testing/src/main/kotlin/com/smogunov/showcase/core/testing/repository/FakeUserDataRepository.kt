package com.smogunov.showcase.core.testing.repository

import com.smogunov.showcase.core.domain.repository.UserDataRepository
import com.smogunov.showcase.core.model.ThemeConfig
import com.smogunov.showcase.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

val defaultUserData = UserData(themeConfig = ThemeConfig.FOLLOW_SYSTEM, useDynamicColor = true)

class FakeUserDataRepository : UserDataRepository {

    private val state = MutableStateFlow(defaultUserData)

    override val userData: Flow<UserData> = state

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        state.update { it.copy(themeConfig = themeConfig) }
    }

    override suspend fun setDynamicColor(useDynamicColor: Boolean) {
        state.update { it.copy(useDynamicColor = useDynamicColor) }
    }
}
