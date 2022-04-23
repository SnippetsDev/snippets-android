package dev.snippets.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import dev.snippets.data.SharedPrefHelper
import dev.snippets.util.State
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repository,
    private val sharedPref: SharedPrefHelper
) : ViewModel() {

    fun getAllSnippets() = liveData {
        emit(State.Loading)
        emit(repo.getAllSnippets())
    }

    fun getSnippetsWithPreferredTags() = liveData {
        emit(State.Loading)
        emit(repo.getSnippetsWithPreferredTags(sharedPref.user.tags.joinToString(",")))
    }

    fun isNewUser() = sharedPref.user.tags.isEmpty()
}