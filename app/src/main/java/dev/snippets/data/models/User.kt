package dev.snippets.data.models

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class User(
    val id: String,
    val name: String,
    val email: String,
    val tags: List<String>,
    val bio: String
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
    return jsonAdapter.fromJson(this) ?: User("", "", "", emptyList(), "")
}