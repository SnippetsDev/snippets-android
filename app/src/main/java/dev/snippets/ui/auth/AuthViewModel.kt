package dev.snippets.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import dev.snippets.data.SharedPrefHelper
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
        emit(State.Success(true))
    }
}