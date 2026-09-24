package com.smogunov.showcase.core.testing.repository

import com.smogunov.showcase.core.domain.repository.FavoritesRepository
import com.smogunov.showcase.core.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeFavoritesRepository : FavoritesRepository {

    private val favorites = MutableStateFlow<List<Character>>(emptyList())

    override fun observeFavorites(): Flow<List<Character>> = favorites

    override fun observeIsFavorite(id: Int): Flow<Boolean> =
        favorites.map { list -> list.any { it.id == id } }.distinctUntilChanged()

    override suspend fun addFavorite(character: Character) {
        favorites.update { it + character }
    }

    override suspend fun removeFavorite(id: Int) {
        favorites.update { list -> list.filterNot { it.id == id } }
    }

    override suspend fun sync(): Boolean = true
}
