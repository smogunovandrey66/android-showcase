package com.smogunov.showcase.feature.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.smogunov.showcase.core.domain.repository.CharacterRepository
import com.smogunov.showcase.core.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    characterRepository: CharacterRepository,
) : ViewModel() {
    /** `cachedIn` keeps loaded pages across configuration changes. */
    val characters: Flow<PagingData<Character>> = characterRepository
        .getCharactersStream()
        .cachedIn(viewModelScope)
}
