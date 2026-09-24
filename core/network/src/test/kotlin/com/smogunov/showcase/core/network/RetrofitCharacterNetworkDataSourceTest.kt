package com.smogunov.showcase.core.network

import com.smogunov.showcase.core.network.di.NetworkModule
import com.smogunov.showcase.core.network.retrofit.RetrofitCharacterNetworkDataSource
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RetrofitCharacterNetworkDataSourceTest {

    private val server = MockWebServer()
    private lateinit var dataSource: CharacterNetworkDataSource

    @Before
    fun setUp() {
        server.start()
        val api = NetworkModule.createApi(
            baseUrl = server.url("/api/").toString(),
            json = NetworkModule.providesJson(),
            okHttpClient = OkHttpClient(),
        )
        dataSource = RetrofitCharacterNetworkDataSource(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getCharactersParsesPageAndSendsPageQuery() = runTest {
        server.enqueue(MockResponse().setBody(readResource("characters_page.json")))

        val page = dataSource.getCharacters(page = 1)

        assertEquals("/api/character?page=1", server.takeRequest().path)
        assertTrue(page.hasNextPage)
        val rick = page.results.single()
        assertEquals("Rick Sanchez", rick.name)
        assertEquals("Earth (C-137)", rick.origin.name)
        assertEquals(2, rick.episodes.size)
    }

    @Test
    fun httpErrorIsMappedToNetworkException() = runTest {
        server.enqueue(MockResponse().setResponseCode(500))

        val error = assertFailsWith<NetworkException> { dataSource.getCharacter(id = 1) }

        assertEquals("HTTP 500", error.message)
    }

    private fun readResource(name: String): String =
        requireNotNull(javaClass.classLoader?.getResource(name)).readText()
}
