package dev.snippets.data

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
    val tags: List<String>
)
