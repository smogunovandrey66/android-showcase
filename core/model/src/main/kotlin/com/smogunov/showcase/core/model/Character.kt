package com.smogunov.showcase.core.model

data class Character(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val gender: String,
    val origin: String,
    val location: String,
    val imageUrl: String,
    val episodeCount: Int,
)

enum class CharacterStatus {
    ALIVE,
    DEAD,
    UNKNOWN,
    ;

    companion object {
        fun fromApiValue(value: String): CharacterStatus = when (value.lowercase()) {
            "alive" -> ALIVE
            "dead" -> DEAD
            else -> UNKNOWN
        }
    }
}
