package com.smogunov.showcase.core.domain.usecase

import com.smogunov.showcase.core.domain.repository.FavoritesRepository
import com.smogunov.showcase.core.model.Character
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {
    suspend operator fun invoke(character: Character, isFavorite: Boolean) {
        if (isFavorite) {
            favoritesRepository.removeFavorite(character.id)
        } else {
            favoritesRepository.addFavorite(character)
        }
    }
}
