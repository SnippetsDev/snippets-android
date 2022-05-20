package dev.snippets.data.network

import dev.snippets.data.models.Snippet
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("/snippets")
    suspend fun getSnippet(@Query("id") id: String): Response<List<Snippet>>

    @GET("/tags")
    suspend fun getAllTags(): Response<List<String>>

    @GET("/snippets")
    suspend fun getSnippetsForTags(
        @Query(
            "tags",
            encoded = true
        ) tags: String
    ): Response<List<Snippet>>

    @POST("/snippets")
    suspend fun publishSnippet(@Body snippet: Snippet): Response<PostResponse>

    @POST("/users")
    suspend fun authenticateUser(@Body authRequestBody: AuthRequestBody): Response<AuthResponse>

    @PATCH("/users")
    suspend fun setPreferredTags(@Body setPreferredTagsRequestBody: SetPreferredTagsRequestBody): Response<PostResponse>
}