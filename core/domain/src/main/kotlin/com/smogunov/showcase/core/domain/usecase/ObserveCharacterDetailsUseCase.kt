package com.smogunov.showcase.core.domain.usecase

import com.smogunov.showcase.core.domain.repository.CharacterRepository
import com.smogunov.showcase.core.domain.repository.FavoritesRepository
import com.smogunov.showcase.core.model.CharacterDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Combines a character with its "favorite" state, so the UI always shows an up-to-date toggle.
 */
class ObserveCharacterDetailsUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val favoritesRepository: FavoritesRepository,
) {
    operator fun invoke(id: Int): Flow<CharacterDetails> = combine(
        flow { emit(characterRepository.getCharacter(id)) },
        favoritesRepository.observeIsFavorite(id),
    ) { character, isFavorite ->
        CharacterDetails(character = character, isFavorite = isFavorite)
    }
}
