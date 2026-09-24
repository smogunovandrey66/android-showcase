package com.smogunov.showcase.core.domain.repository

import com.smogunov.showcase.core.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<Character>>

    fun observeIsFavorite(id: Int): Flow<Boolean>

    suspend fun addFavorite(character: Character)

    suspend fun removeFavorite(id: Int)

    /** Refreshes favorites with fresh data from the network. Returns `true` on success. */
    suspend fun sync(): Boolean
}
