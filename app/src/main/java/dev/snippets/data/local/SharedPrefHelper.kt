package dev.snippets.data.local

import android.content.Context
import androidx.core.content.edit
import dev.snippets.data.models.User
import dev.snippets.data.models.toJson
import dev.snippets.data.models.toUser
import dev.snippets.util.Constants
import dev.snippets.util.toStringWithCommas

class SharedPrefHelper(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("dev.snippets.data", Context.MODE_PRIVATE)

    fun publishedFirstSnippet(): Boolean {
        if (sharedPreferences.getBoolean(Constants.KEY_PUBLISHED_FIRST_SNIPPET, true)) {
            sharedPreferences.edit {
                putBoolean(Constants.KEY_PUBLISHED_FIRST_SNIPPET, false)
            }
            return true
        }
        return false
    }

    var user: User
        get() = (sharedPreferences.getString(Constants.KEY_USER, "") ?: "").toUser()
        set(value) = sharedPreferences.edit {
            putString(Constants.KEY_USER, value.toJson())
        }

    var accessToken: String
        get() = (sharedPreferences.getString(Constants.KEY_ACCESS_TOKEN, "") ?: "")
        set(value) = sharedPreferences.edit {
            putString(Constants.KEY_ACCESS_TOKEN, value)
        }

    fun nukePreferenceData() {
        sharedPreferences.edit {
            clear()
        }
    }
}
