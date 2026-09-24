package com.smogunov.showcase.core.network.di

import com.smogunov.showcase.core.network.BuildConfig
import com.smogunov.showcase.core.network.CharacterNetworkDataSource
import com.smogunov.showcase.core.network.retrofit.RetrofitCharacterNetworkDataSource
import com.smogunov.showcase.core.network.retrofit.RickAndMortyApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            },
        )
        .build()

    @Provides
    @Singleton
    fun providesRickAndMortyApi(json: Json, okHttpClient: OkHttpClient): RickAndMortyApi =
        createApi(BuildConfig.BASE_URL, json, okHttpClient)

    fun createApi(baseUrl: String, json: Json, okHttpClient: OkHttpClient): RickAndMortyApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RickAndMortyApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkBindsModule {
    @Binds
    fun bindsCharacterNetworkDataSource(impl: RetrofitCharacterNetworkDataSource): CharacterNetworkDataSource
}
