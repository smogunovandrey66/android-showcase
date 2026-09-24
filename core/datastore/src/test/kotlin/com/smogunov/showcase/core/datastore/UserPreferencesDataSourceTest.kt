package com.smogunov.showcase.core.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.smogunov.showcase.core.model.ThemeConfig
import com.smogunov.showcase.core.model.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class UserPreferencesDataSourceTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private fun TestScope.createDataSource() = UserPreferencesDataSource(
        PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tmpFolder.root, "test.preferences_pb") },
        ),
    )

    @Test
    fun defaultsAreUsedWhenNothingIsStored() = runTest {
        val dataSource = createDataSource()

        assertEquals(
            UserData(themeConfig = ThemeConfig.FOLLOW_SYSTEM, useDynamicColor = true),
            dataSource.userData.first(),
        )
    }

    @Test
    fun changesArePersisted() = runTest {
        val dataSource = createDataSource()

        dataSource.setThemeConfig(ThemeConfig.DARK)
        dataSource.setDynamicColor(false)

        assertEquals(
            UserData(themeConfig = ThemeConfig.DARK, useDynamicColor = false),
            dataSource.userData.first(),
        )
    }
}
