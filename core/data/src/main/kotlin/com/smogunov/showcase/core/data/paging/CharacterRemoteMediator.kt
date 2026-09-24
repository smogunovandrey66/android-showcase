package com.smogunov.showcase.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.smogunov.showcase.core.data.model.asEntity
import com.smogunov.showcase.core.database.ShowcaseDatabase
import com.smogunov.showcase.core.database.model.CharacterEntity
import com.smogunov.showcase.core.database.model.RemoteKeyEntity
import com.smogunov.showcase.core.network.CharacterNetworkDataSource
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Offline-first paging: the UI always reads from Room, this mediator fetches pages from the
 * network and writes them to the database (single source of truth).
 */
@OptIn(ExperimentalPagingApi::class)
internal class CharacterRemoteMediator(
    private val database: ShowcaseDatabase,
    private val network: CharacterNetworkDataSource,
    private val clock: () -> Long = System::currentTimeMillis,
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = database.characterDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val lastUpdated = remoteKeyDao.lastUpdated() ?: return InitializeAction.LAUNCH_INITIAL_REFRESH
        return if (clock() - lastUpdated < CACHE_TIMEOUT_MILLIS) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CharacterEntity>): MediatorResult {
        // null means there is nothing more to load in this direction.
        val page = pageToLoad(loadType, state) ?: return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val response = network.getCharacters(page)
            val nextPage = if (response.hasNextPage) page + 1 else null
            val now = clock()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clear()
                    characterDao.clear()
                }
                remoteKeyDao.upsertAll(
                    response.results.map {
                        RemoteKeyEntity(
                            characterId = it.id,
                            prevPage = (page - 1).takeIf { prev -> prev >= FIRST_PAGE },
                            nextPage = nextPage,
                            createdAt = now,
                        )
                    },
                )
                characterDao.upsertAll(response.results.map { it.asEntity() })
            }
            MediatorResult.Success(endOfPaginationReached = nextPage == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun pageToLoad(loadType: LoadType, state: PagingState<Int, CharacterEntity>): Int? =
        when (loadType) {
            LoadType.REFRESH -> FIRST_PAGE
            LoadType.PREPEND -> null
            LoadType.APPEND -> state.lastItemOrNull()?.let { remoteKeyDao.getByCharacterId(it.id)?.nextPage }
        }

    private companion object {
        const val FIRST_PAGE = 1
        val CACHE_TIMEOUT_MILLIS = TimeUnit.HOURS.toMillis(1)
    }
}
