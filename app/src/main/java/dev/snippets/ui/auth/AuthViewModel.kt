package dev.snippets.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import dev.snippets.data.SharedPrefHelper
import dev.snippets.util.State
import dev.snippets.util.log
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
            listTags = response.data.toMutableList()
            emit(State.Success(response.data))
        } else if (response is State.Error) {
            emit(State.Error(response.message))
        }
    }

    fun setPreferredTags() = liveData {
        emit(State.Loading)
        val response = repo.setPreferredTags(sharedPref.user.id, listTags)
        if (response is State.Success) {
            sharedPref.user = sharedPref.user.copy(tags = listTags)
            emit(State.Success(response.data))
        } else if (response is State.Error) {
            emit(State.Error(response.message))
        }
    }

    fun loginWithGithub(code: String) = liveData {
        log("Received access code from GitHub: $code")
        emit(State.Loading)
        val response = repo.authenticateUser(code)
        if (response is State.Success) {
            sharedPref.user = response.data.user
            sharedPref.accessToken = response.data.access_token
            emit(State.Success(true))
        } else {
            emit(State.Error("Failed to login"))
        }
    }

    fun isNewUser() = sharedPref.user.tags.isEmpty()
}