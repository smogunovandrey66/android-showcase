package com.smogunov.showcase.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smogunov.showcase.core.model.Character
import com.smogunov.showcase.core.model.CharacterStatus

/**
 * A full snapshot of a favorite character: favorites must survive cache refreshes and work offline.
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val gender: String,
    val origin: String,
    val location: String,
    val imageUrl: String,
    val episodeCount: Int,
    val addedAt: Long,
)

fun FavoriteEntity.asExternalModel() = Character(
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

fun Character.asFavoriteEntity(addedAt: Long) = FavoriteEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    origin = origin,
    location = location,
    imageUrl = imageUrl,
    episodeCount = episodeCount,
    addedAt = addedAt,
)
