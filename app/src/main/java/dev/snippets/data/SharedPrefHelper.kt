package dev.snippets.data

import android.content.Context
import androidx.core.content.edit
import dev.snippets.util.Constants

class SharedPrefHelper(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("dev.snippets.data", Context.MODE_PRIVATE)

    fun publishedFirstSnippet(): Boolean {
        if (sharedPreferences.getBoolean(Constants.KEY_PUBLISHED_FIRST_SNIPPET, true)) {
            sharedPreferences.edit {
                putBoolean(Constants.KEY_PUBLISHED_FIRST_SNIPPET, false)
            }
            return true
        }
        return false
    }
}
