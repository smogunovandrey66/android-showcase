package com.smogunov.showcase.core.network.retrofit

import com.smogunov.showcase.core.network.CharacterNetworkDataSource
import com.smogunov.showcase.core.network.NetworkException
import com.smogunov.showcase.core.network.model.NetworkCharacter
import com.smogunov.showcase.core.network.model.NetworkCharacterPage
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import javax.inject.Inject

internal interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): NetworkCharacterPage

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): NetworkCharacter
}

internal class RetrofitCharacterNetworkDataSource @Inject constructor(
    private val api: RickAndMortyApi,
) : CharacterNetworkDataSource {

    override suspend fun getCharacters(page: Int): NetworkCharacterPage = safeApiCall { api.getCharacters(page) }

    override suspend fun getCharacter(id: Int): NetworkCharacter = safeApiCall { api.getCharacter(id) }

    private suspend fun <T> safeApiCall(block: suspend () -> T): T {
        val error = try {
            return block()
        } catch (e: HttpException) {
            NetworkException("HTTP ${e.code()}", e)
        } catch (e: SerializationException) {
            NetworkException("Malformed response", e)
        } catch (e: IOException) {
            NetworkException(e.message, e)
        }
        throw error
    }
}
