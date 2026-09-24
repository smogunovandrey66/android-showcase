package com.smogunov.showcase.core.data.repository

import com.smogunov.showcase.core.datastore.UserPreferencesDataSource
import com.smogunov.showcase.core.domain.repository.UserDataRepository
import com.smogunov.showcase.core.model.ThemeConfig
import com.smogunov.showcase.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultUserDataRepository @Inject constructor(
    private val preferences: UserPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> = preferences.userData

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) = preferences.setThemeConfig(themeConfig)

    override suspend fun setDynamicColor(useDynamicColor: Boolean) = preferences.setDynamicColor(useDynamicColor)
}
