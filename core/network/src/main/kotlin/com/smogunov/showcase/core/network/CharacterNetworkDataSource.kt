package com.smogunov.showcase.core.network

import com.smogunov.showcase.core.network.model.NetworkCharacter
import com.smogunov.showcase.core.network.model.NetworkCharacterPage

/**
 * Abstraction over the remote API. The data layer depends on this interface only,
 * so the HTTP client (Retrofit, Ktor, ...) can be replaced or faked in tests.
 *
 * All methods throw [NetworkException] on failure.
 */
interface CharacterNetworkDataSource {
    suspend fun getCharacters(page: Int): NetworkCharacterPage

    suspend fun getCharacter(id: Int): NetworkCharacter
}
