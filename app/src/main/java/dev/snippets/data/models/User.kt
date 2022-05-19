package dev.snippets.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.EOFException

data class User(
    val id: Long,
    @Json(name = "avatar_url") val imageUrl: String,
    val bio: String,
    @Json(name = "login") val username: String,
    val email: String?,
    val name: String,
    val tags: List<String>
)

fun User.toJson(): String {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(User::class.java)
    return jsonAdapter.toJson(this) ?: ""
}

fun String.toUser(): User {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(User::class.java)
    return try {
        jsonAdapter.fromJson(this) ?: User(0, "", "", "", "", "", emptyList())
    } catch (e: EOFException) { // In case of empty json, meaning no user is currently saved
        User(0, "", "", "", "", "", emptyList())
    } catch (e: JsonDataException) {
        // Happening only on Samsung A70, very weird.
        User(0, "", "", "", "", "", emptyList())
    }
}