package com.smogunov.showcase.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCharacterPage(
    val info: NetworkPageInfo,
    val results: List<NetworkCharacter>,
) {
    /** `null` when this is the last page. */
    val hasNextPage: Boolean get() = info.next != null
}

@Serializable
data class NetworkPageInfo(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null,
)

@Serializable
data class NetworkCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: NetworkLocationRef,
    val location: NetworkLocationRef,
    @SerialName("image") val imageUrl: String,
    @SerialName("episode") val episodes: List<String> = emptyList(),
)

@Serializable
data class NetworkLocationRef(
    val name: String,
    val url: String = "",
)
