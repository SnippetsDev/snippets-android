package dev.snippets.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "table_snippets")
data class Snippet(
    @PrimaryKey val id: String,
    val title: String,
    val language: String,
    val description: String,
    val tags: List<String>,
    @Json(name = "image_url") val imageUrl: String?,
    val code: String?,
    val userId: Long
)
