package dev.snippets.data.network

import dev.snippets.data.models.User

data class PostResponse(val error: String?)

data class AuthRequestBody(val access_code: String)

data class AuthResponse(
    val access_token: String,
    val user: User
)

data class SetPreferredTagsRequestBody(
    val id: Long,
    val tags: List<String>
)