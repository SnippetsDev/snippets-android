package dev.snippets.data

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

    @Deprecated("Shift to user model")
    fun isNewUser() =
        sharedPreferences.getString(Constants.KEY_USER_PREFERRED_TAGS, "").isNullOrEmpty()

    @Deprecated("Shift to user model")
    fun saveUserPreferredTags(tags: List<String>) {
        sharedPreferences.edit {
            putString(Constants.KEY_USER_PREFERRED_TAGS, tags.toStringWithCommas())
        }
    }

    fun getUserPreferredTags() =
        sharedPreferences.getString(Constants.KEY_USER_PREFERRED_TAGS, "")!!

    var user: User
        get() = (sharedPreferences.getString(Constants.KEY_USER, "") ?: "").toUser()
        set(value) = sharedPreferences.edit {
            putString(Constants.KEY_USER, value.toJson())
        }
}
