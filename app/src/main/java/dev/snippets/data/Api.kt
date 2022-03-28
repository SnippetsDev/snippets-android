package dev.snippets.data

import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("/snippets")
    suspend fun getAllSnippets(): Response<SnippetsApiResponse>

    @GET("/snippets")
    suspend fun getSnippet(@Query("id") id: String): Response<SnippetsApiResponse>

    @GET("/tags")
    suspend fun getAllTags(): Response<TagsApiResponse>

    @GET("/snippets")
    suspend fun getSnippetsForTags(@Query("tags", encoded = true) tags: String): Response<SnippetsApiResponse>

    @POST("/snippets")
    suspend fun publishSnippet(@Body snippet: Snippet): Response<PostResponse>
}