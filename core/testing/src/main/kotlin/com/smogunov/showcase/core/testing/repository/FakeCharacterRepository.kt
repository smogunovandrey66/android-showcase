package com.smogunov.showcase.core.testing.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.smogunov.showcase.core.domain.repository.CharacterRepository
import com.smogunov.showcase.core.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCharacterRepository : CharacterRepository {

    private val characters = MutableStateFlow<List<Character>>(emptyList())

    /** When set, [getCharacter] throws it instead of returning data. */
    var error: Throwable? = null

    fun sendCharacters(list: List<Character>) {
        characters.value = list
    }

    // A real Pager over a single in-memory page: exercises the same paging machinery as production.
    override fun getCharactersStream(): Flow<PagingData<Character>> =
        Pager(PagingConfig(pageSize = PAGE_SIZE)) { SinglePagePagingSource(characters.value) }.flow

    override suspend fun getCharacter(id: Int): Character {
        error?.let { throw it }
        return characters.value.first { it.id == id }
    }

    private class SinglePagePagingSource(
        private val items: List<Character>,
    ) : PagingSource<Int, Character>() {
        override fun getRefreshKey(state: PagingState<Int, Character>): Int? = null

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> =
            LoadResult.Page(data = items, prevKey = null, nextKey = null)
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
