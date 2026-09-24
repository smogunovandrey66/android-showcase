package com.smogunov.showcase.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.smogunov.showcase.core.database.dao.FavoriteDao
import com.smogunov.showcase.core.database.model.FavoriteEntity
import com.smogunov.showcase.core.model.CharacterStatus
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Room DAO test running on the JVM thanks to Robolectric — no emulator needed. */
@RunWith(RobolectricTestRunner::class)
class FavoriteDaoTest {

    private lateinit var database: ShowcaseDatabase
    private lateinit var dao: FavoriteDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShowcaseDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = database.favoriteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun favoritesAreOrderedByMostRecentlyAdded() = runTest {
        dao.upsert(favorite(id = 1, addedAt = 100))
        dao.upsert(favorite(id = 2, addedAt = 200))

        dao.observeAll().test {
            assertEquals(listOf(2, 1), awaitItem().map { it.id })
        }
    }

    @Test
    fun observeIsFavoriteReflectsInsertAndDelete() = runTest {
        dao.observeIsFavorite(id = 1).test {
            assertFalse(awaitItem())

            dao.upsert(favorite(id = 1, addedAt = 100))
            assertTrue(awaitItem())

            dao.delete(id = 1)
            assertFalse(awaitItem())
        }
    }

    private fun favorite(id: Int, addedAt: Long) = FavoriteEntity(
        id = id,
        name = "Character $id",
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = "Male",
        origin = "Earth",
        location = "Earth",
        imageUrl = "",
        episodeCount = 1,
        addedAt = addedAt,
    )
}
