package com.smogunov.showcase.core.testing.repository

import androidx.paging.PagingData
import com.smogunov.showcase.core.domain.repository.CharacterRepository
import com.smogunov.showcase.core.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCharacterRepository : CharacterRepository {

    private val characters = MutableStateFlow<List<Character>>(emptyList())

    /** When set, [getCharacter] throws it instead of returning data. */
    var error: Throwable? = null

    fun sendCharacters(list: List<Character>) {
        characters.value = list
    }

    override fun getCharactersStream(): Flow<PagingData<Character>> =
        characters.map { PagingData.from(it) }

    override suspend fun getCharacter(id: Int): Character {
        error?.let { throw it }
        return characters.value.first { it.id == id }
    }
}
