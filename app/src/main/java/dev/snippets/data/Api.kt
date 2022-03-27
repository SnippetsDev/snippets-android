package dev.snippets.data

import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET("/snippets")
    suspend fun getAllSnippets(): Response<SnippetsApiResponse>

    @GET("/tags")
    suspend fun getAllTags(): Response<TagsApiResponse>
}