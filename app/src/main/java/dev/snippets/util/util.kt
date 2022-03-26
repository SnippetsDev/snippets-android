package dev.snippets.util

import android.util.Log
import dev.snippets.BuildConfig

fun log(vararg messages: String) {
    if (BuildConfig.DEBUG) {
        for (message in messages) {
            Log.d("SnippetsDebug", message)
        }
    }
}