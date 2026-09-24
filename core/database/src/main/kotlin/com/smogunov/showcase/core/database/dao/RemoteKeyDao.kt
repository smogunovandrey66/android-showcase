package com.smogunov.showcase.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.smogunov.showcase.core.database.model.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Query("SELECT * FROM remote_keys WHERE characterId = :characterId")
    suspend fun getByCharacterId(characterId: Int): RemoteKeyEntity?

    @Query("SELECT MAX(createdAt) FROM remote_keys")
    suspend fun lastUpdated(): Long?

    @Upsert
    suspend fun upsertAll(keys: List<RemoteKeyEntity>)

    @Query("DELETE FROM remote_keys")
    suspend fun clear()
}
