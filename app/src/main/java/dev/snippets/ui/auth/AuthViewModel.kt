package dev.snippets.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import dev.snippets.data.SharedPrefHelper
import dev.snippets.data.models.User
import dev.snippets.util.State
import dev.snippets.util.log
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

    fun setPreferredTags() {
        sharedPref.user = sharedPref.user.copy(tags = listTags)
    }

    fun loginWithGithub(token: String) = liveData {
        log("Received temp code from GitHub: $token")
        emit(State.Loading)
        delay(1000)
        sharedPref.user = User(
            "0",
            "Linus Torvalds",
            "torvalds@linux-foundation.org",
            "https://i.pcmag.com/imagery/articles/040JHoVNgc1gh2e7sunj82k-1.fit_lim.size_1600x900.v1569492349.png",
            "Creator and Maintainer of Linux and Git, basically the reason for most developers' jobs",
            emptyList()
        )
        emit(State.Success(true))
    }

    fun isNewUser() = sharedPref.user.tags.isEmpty()
}