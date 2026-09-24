package com.smogunov.showcase.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smogunov.showcase.core.model.Character
import com.smogunov.showcase.core.model.CharacterStatus

/** Cached page item. The table is cleared and refilled by the paging RemoteMediator. */
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val gender: String,
    val origin: String,
    val location: String,
    val imageUrl: String,
    val episodeCount: Int,
)

fun CharacterEntity.asExternalModel() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    origin = origin,
    location = location,
    imageUrl = imageUrl,
    episodeCount = episodeCount,
)
