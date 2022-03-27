package dev.snippets.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @GET("/snippets")
    suspend fun getAllSnippets(): Response<SnippetsApiResponse>

    @GET("/tags")
    suspend fun getAllTags(): Response<TagsApiResponse>

    @POST("/snippets")
    suspend fun publishSnippet(@Body snippet: Snippet): Response<PostResponse>
}