package com.smogunov.showcase.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.smogunov.showcase.core.data.model.asExternalModel
import com.smogunov.showcase.core.data.paging.CharacterRemoteMediator
import com.smogunov.showcase.core.database.ShowcaseDatabase
import com.smogunov.showcase.core.database.model.asExternalModel
import com.smogunov.showcase.core.domain.repository.CharacterRepository
import com.smogunov.showcase.core.model.Character
import com.smogunov.showcase.core.network.CharacterNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class OfflineFirstCharacterRepository @Inject constructor(
    private val database: ShowcaseDatabase,
    private val network: CharacterNetworkDataSource,
) : CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharactersStream(): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        remoteMediator = CharacterRemoteMediator(database, network),
        pagingSourceFactory = { database.characterDao().pagingSource() },
    ).flow.map { pagingData -> pagingData.map { it.asExternalModel() } }

    override suspend fun getCharacter(id: Int): Character =
        database.characterDao().getById(id)?.asExternalModel()
            ?: database.favoriteDao().getById(id)?.asExternalModel()
            ?: network.getCharacter(id).asExternalModel()

    private companion object {
        // Matches the page size of the Rick and Morty API.
        const val PAGE_SIZE = 20
    }
}
