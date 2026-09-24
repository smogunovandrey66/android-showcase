package com.smogunov.showcase.feature.favorites

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smogunov.showcase.core.designsystem.theme.ShowcaseTheme
import com.smogunov.showcase.core.testing.data.testCharacter
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals

/** Compose UI test executed on the JVM by Robolectric (runs in CI without an emulator). */
@RunWith(AndroidJUnit4::class)
class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun string(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun loadingStateShowsProgress() {
        composeTestRule.setContent {
            ShowcaseTheme(dynamicColor = false) {
                FavoritesContent(uiState = FavoritesUiState.Loading, onCharacterClick = {})
            }
        }

        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun emptyStateShowsMessage() {
        composeTestRule.setContent {
            ShowcaseTheme(dynamicColor = false) {
                FavoritesContent(uiState = FavoritesUiState.Empty, onCharacterClick = {})
            }
        }

        composeTestRule.onNodeWithText(string(R.string.feature_favorites_empty)).assertIsDisplayed()
    }

    @Test
    fun clickOnFavoriteReportsItsId() {
        var clickedId: Int? = null
        composeTestRule.setContent {
            ShowcaseTheme(dynamicColor = false) {
                FavoritesContent(
                    uiState = FavoritesUiState.Success(listOf(testCharacter(id = 42, name = "Morty Smith"))),
                    onCharacterClick = { clickedId = it },
                )
            }
        }

        composeTestRule.onNodeWithText("Morty Smith").performClick()

        assertEquals(42, clickedId)
    }
}
