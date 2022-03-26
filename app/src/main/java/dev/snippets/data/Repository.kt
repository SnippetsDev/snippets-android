package dev.snippets.data

import dev.snippets.util.log

class Repository (
    private val api: Api
) {
    suspend fun getAllSnippets() {
        log(api.getAllSnippets().body().toString())
    }
}