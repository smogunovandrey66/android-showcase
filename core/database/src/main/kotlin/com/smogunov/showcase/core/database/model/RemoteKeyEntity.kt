package com.smogunov.showcase.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Paging bookkeeping: which API page a cached character came from and when it was fetched. */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val characterId: Int,
    val prevPage: Int?,
    val nextPage: Int?,
    val createdAt: Long,
)
