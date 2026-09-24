package com.smogunov.showcase.core.data

import com.smogunov.showcase.core.data.repository.OfflineFirstFavoritesRepository
import com.smogunov.showcase.core.database.dao.FavoriteDao
import com.smogunov.showcase.core.database.model.FavoriteEntity
import com.smogunov.showcase.core.model.CharacterStatus
import com.smogunov.showcase.core.network.CharacterNetworkDataSource
import com.smogunov.showcase.core.network.NetworkException
import com.smogunov.showcase.core.network.model.NetworkCharacter
import com.smogunov.showcase.core.network.model.NetworkLocationRef
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OfflineFirstFavoritesRepositoryTest {

    private val favoriteDao = mockk<FavoriteDao>(relaxUnitFun = true)
    private val network = mockk<CharacterNetworkDataSource>()

    private fun createRepository() = OfflineFirstFavoritesRepository(
        favoriteDao = favoriteDao,
        network = network,
        ioDispatcher = UnconfinedTestDispatcher(),
    )

    @Test
    fun syncUpdatesFavoritesAndKeepsAddedAt() = runTest {
        coEvery { favoriteDao.getAll() } returns listOf(favorite(id = 1, name = "Old name", addedAt = 42))
        coEvery { network.getCharacter(1) } returns networkCharacter(id = 1, name = "New name")
        val saved = slot<List<FavoriteEntity>>()
        coEvery { favoriteDao.upsertAll(capture(saved)) } returns Unit

        val result = createRepository().sync()

        assertTrue(result)
        val updated = saved.captured.single()
        assertEquals("New name", updated.name)
        assertEquals(CharacterStatus.DEAD, updated.status)
        assertEquals(42, updated.addedAt)
    }

    @Test
    fun syncReturnsFalseOnNetworkError() = runTest {
        coEvery { favoriteDao.getAll() } returns listOf(favorite(id = 1, name = "Rick", addedAt = 1))
        coEvery { network.getCharacter(any()) } throws NetworkException("offline")

        val result = createRepository().sync()

        assertFalse(result)
        coVerify(exactly = 0) { favoriteDao.upsertAll(any()) }
    }

    private fun favorite(id: Int, name: String, addedAt: Long) = FavoriteEntity(
        id = id,
        name = name,
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = "Male",
        origin = "Earth",
        location = "Earth",
        imageUrl = "",
        episodeCount = 1,
        addedAt = addedAt,
    )

    private fun networkCharacter(id: Int, name: String) = NetworkCharacter(
        id = id,
        name = name,
        status = "Dead",
        species = "Human",
        gender = "Male",
        origin = NetworkLocationRef(name = "Earth"),
        location = NetworkLocationRef(name = "Earth"),
        imageUrl = "",
        episodes = listOf("e1", "e2"),
    )
}
