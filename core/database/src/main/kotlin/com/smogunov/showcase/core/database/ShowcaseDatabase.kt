package com.smogunov.showcase.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smogunov.showcase.core.database.dao.CharacterDao
import com.smogunov.showcase.core.database.dao.FavoriteDao
import com.smogunov.showcase.core.database.dao.RemoteKeyDao
import com.smogunov.showcase.core.database.model.CharacterEntity
import com.smogunov.showcase.core.database.model.FavoriteEntity
import com.smogunov.showcase.core.database.model.RemoteKeyEntity

@Database(
    entities = [
        CharacterEntity::class,
        FavoriteEntity::class,
        RemoteKeyEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class ShowcaseDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    abstract fun favoriteDao(): FavoriteDao

    abstract fun remoteKeyDao(): RemoteKeyDao
}
