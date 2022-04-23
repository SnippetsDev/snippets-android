package dev.snippets.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import dev.snippets.data.SharedPrefHelper
import dev.snippets.data.models.User
import dev.snippets.util.State
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: Repository,
    private val sharedPref: SharedPrefHelper
) : ViewModel() {
    var listTags = mutableListOf<String>()

    fun getAllTags() = liveData {
        emit(State.Loading)
        val response = repo.getAllTags()
        if (response is State.Success) {
            listTags = response.data.tags.toMutableList()
            emit(State.Success(response.data))
        } else if (response is State.Error) {
            emit(State.Error(response.message))
        }
    }

    fun setPreferredTags() = sharedPref.saveUserPreferredTags(listTags)

    fun loginWithGithub(token: String) = liveData {
        emit(State.Loading)
        delay(1000)
        sharedPref.user = User(
            "0",
            "Guido van Rossum",
            "guido@python.org",
            "https://pbs.twimg.com/profile_images/424495004/GuidoAvatar_400x400.jpg",
            "Creator of the Python programming language.\n" +
                    "        Holds a Master\\'s degree in Computer Science and Mathematics.\n" +
                    "        Previously Senior Staff Engineer @ Google, Principal Engineer @ Dropbox.\n" +
                    "        Currently Distinguished Engineer @ Microsoft.",
            listOf("Android", "Python", "Kotlin")
        )
        emit(State.Success(true))
    }

    fun isNewUser() = sharedPref.user.tags.isEmpty()
}