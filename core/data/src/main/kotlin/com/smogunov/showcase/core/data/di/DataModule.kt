package com.smogunov.showcase.core.data.di

import com.smogunov.showcase.core.data.repository.DefaultUserDataRepository
import com.smogunov.showcase.core.data.repository.OfflineFirstCharacterRepository
import com.smogunov.showcase.core.data.repository.OfflineFirstFavoritesRepository
import com.smogunov.showcase.core.domain.repository.CharacterRepository
import com.smogunov.showcase.core.domain.repository.FavoritesRepository
import com.smogunov.showcase.core.domain.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun bindsCharacterRepository(impl: OfflineFirstCharacterRepository): CharacterRepository

    @Binds
    fun bindsFavoritesRepository(impl: OfflineFirstFavoritesRepository): FavoritesRepository

    @Binds
    fun bindsUserDataRepository(impl: DefaultUserDataRepository): UserDataRepository
}
