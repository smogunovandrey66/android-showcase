package com.smogunov.showcase.core.data.model

import com.smogunov.showcase.core.database.model.CharacterEntity
import com.smogunov.showcase.core.model.Character
import com.smogunov.showcase.core.model.CharacterStatus
import com.smogunov.showcase.core.network.model.NetworkCharacter

internal fun NetworkCharacter.asEntity() = CharacterEntity(
    id = id,
    name = name,
    status = CharacterStatus.fromApiValue(status),
    species = species,
    gender = gender,
    origin = origin.name,
    location = location.name,
    imageUrl = imageUrl,
    episodeCount = episodes.size,
)

internal fun NetworkCharacter.asExternalModel() = Character(
    id = id,
    name = name,
    status = CharacterStatus.fromApiValue(status),
    species = species,
    gender = gender,
    origin = origin.name,
    location = location.name,
    imageUrl = imageUrl,
    episodeCount = episodes.size,
)
