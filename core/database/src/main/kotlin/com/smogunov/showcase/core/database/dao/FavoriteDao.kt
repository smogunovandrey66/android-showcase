package com.smogunov.showcase.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.smogunov.showcase.core.database.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    fun observeIsFavorite(id: Int): Flow<Boolean>

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getById(id: Int): FavoriteEntity?

    @Query("SELECT * FROM favorites")
    suspend fun getAll(): List<FavoriteEntity>

    @Upsert
    suspend fun upsert(favorite: FavoriteEntity)

    @Upsert
    suspend fun upsertAll(favorites: List<FavoriteEntity>)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun delete(id: Int)
}
