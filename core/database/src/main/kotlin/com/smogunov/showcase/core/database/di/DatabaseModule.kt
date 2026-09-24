package com.smogunov.showcase.core.database.di

import android.content.Context
import androidx.room.Room
import com.smogunov.showcase.core.database.ShowcaseDatabase
import com.smogunov.showcase.core.database.dao.CharacterDao
import com.smogunov.showcase.core.database.dao.FavoriteDao
import com.smogunov.showcase.core.database.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): ShowcaseDatabase =
        Room.databaseBuilder(context, ShowcaseDatabase::class.java, "showcase-database").build()

    @Provides
    fun providesCharacterDao(database: ShowcaseDatabase): CharacterDao = database.characterDao()

    @Provides
    fun providesFavoriteDao(database: ShowcaseDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun providesRemoteKeyDao(database: ShowcaseDatabase): RemoteKeyDao = database.remoteKeyDao()
}
