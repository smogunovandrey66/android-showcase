package com.smogunov.showcase.core.testing.data

import com.smogunov.showcase.core.model.Character
import com.smogunov.showcase.core.model.CharacterStatus

fun testCharacter(
    id: Int = 1,
    name: String = "Rick Sanchez",
    status: CharacterStatus = CharacterStatus.ALIVE,
) = Character(
    id = id,
    name = name,
    status = status,
    species = "Human",
    gender = "Male",
    origin = "Earth (C-137)",
    location = "Citadel of Ricks",
    imageUrl = "https://rickandmortyapi.com/api/character/avatar/$id.jpeg",
    episodeCount = 51,
)
