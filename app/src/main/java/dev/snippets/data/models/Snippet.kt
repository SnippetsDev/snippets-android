package dev.snippets.data.models

import com.squareup.moshi.Json

data class SnippetsApiResponse(
    val snippets: List<Snippet>,
)

data class Snippet(
    val id: String,
    val title: String,
    val language: String,
    val description: String,
    val tags: List<String>,
    @Json(name = "img_url") val imageUrl: String?,
    val code: String?
)
