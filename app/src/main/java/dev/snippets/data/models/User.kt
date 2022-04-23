package dev.snippets.data.models

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.EOFException

data class User(
    val id: String,
    val name: String,
    val email: String,
    val imageUrl: String,
    val bio: String,
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
        jsonAdapter.fromJson(this) ?: User("", "", "", "", "", emptyList())
    } catch (e: EOFException) { // In case of empty json, meaning no user is currently saved
        User("", "", "", "", "", emptyList())
    }
}