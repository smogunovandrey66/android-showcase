package com.smogunov.showcase.core.data.repository

import com.smogunov.showcase.core.common.di.Dispatcher
import com.smogunov.showcase.core.common.di.ShowcaseDispatchers
import com.smogunov.showcase.core.data.model.asExternalModel
import com.smogunov.showcase.core.database.dao.FavoriteDao
import com.smogunov.showcase.core.database.model.asExternalModel
import com.smogunov.showcase.core.database.model.asFavoriteEntity
import com.smogunov.showcase.core.domain.repository.FavoritesRepository
import com.smogunov.showcase.core.model.Character
import com.smogunov.showcase.core.network.CharacterNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

internal class OfflineFirstFavoritesRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val network: CharacterNetworkDataSource,
    @Dispatcher(ShowcaseDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<Character>> =
        favoriteDao.observeAll().map { favorites -> favorites.map { it.asExternalModel() } }

    override fun observeIsFavorite(id: Int): Flow<Boolean> =
        favoriteDao.observeIsFavorite(id).distinctUntilChanged()

    override suspend fun addFavorite(character: Character) {
        favoriteDao.upsert(character.asFavoriteEntity(addedAt = System.currentTimeMillis()))
    }

    override suspend fun removeFavorite(id: Int) {
        favoriteDao.delete(id)
    }

    override suspend fun sync(): Boolean = withContext(ioDispatcher) {
        try {
            val stored = favoriteDao.getAll()
            val fresh = coroutineScope {
                stored.map { favorite ->
                    async { network.getCharacter(favorite.id).asExternalModel().asFavoriteEntity(favorite.addedAt) }
                }.awaitAll()
            }
            favoriteDao.upsertAll(fresh)
            true
        } catch (ignored: IOException) {
            false
        }
    }
}
