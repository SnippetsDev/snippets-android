package dev.snippets.data

import com.squareup.moshi.Json

data class SnippetsApiResponse(
    val error: Any?,
    val snippets: List<Snippet>,
    val transaction: Boolean
)

data class Snippet(
    val id: String,
    val title: String,
    val language: String,
    val description: String,
    val tags: List<String>,
    @Json(name = "img_url") val imageUrl: String,
    val code: String
)
