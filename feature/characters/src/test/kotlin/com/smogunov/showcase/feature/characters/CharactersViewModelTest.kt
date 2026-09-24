package com.smogunov.showcase.feature.characters

import androidx.paging.testing.asSnapshot
import com.smogunov.showcase.core.testing.data.testCharacter
import com.smogunov.showcase.core.testing.repository.FakeCharacterRepository
import com.smogunov.showcase.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class CharactersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCharacterRepository()

    @Test
    fun charactersArePagedFromRepository() = runTest {
        repository.sendCharacters(listOf(testCharacter(id = 1), testCharacter(id = 2)))
        val viewModel = CharactersViewModel(repository)

        val snapshot = viewModel.characters.asSnapshot()

        assertEquals(listOf(1, 2), snapshot.map { it.id })
    }
}
