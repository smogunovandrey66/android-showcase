package com.smogunov.showcase.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.smogunov.showcase.core.database.model.CharacterEntity

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun pagingSource(): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getById(id: Int): CharacterEntity?

    @Upsert
    suspend fun upsertAll(characters: List<CharacterEntity>)

    @Query("DELETE FROM characters")
    suspend fun clear()
}
