package com.smogunov.showcase.core.domain.repository

import androidx.paging.PagingData
import com.smogunov.showcase.core.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    /** Paged, offline-first stream of characters (network + local cache). */
    fun getCharactersStream(): Flow<PagingData<Character>>

    /** Returns a character from the cache if possible, otherwise from the network. */
    suspend fun getCharacter(id: Int): Character
}
