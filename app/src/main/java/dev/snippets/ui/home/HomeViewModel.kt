package dev.snippets.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import dev.snippets.data.local.SharedPrefHelper
import dev.snippets.util.State
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repository,
    private val sharedPref: SharedPrefHelper
) : ViewModel() {

    fun getSnippetsWithPreferredTags(forceRefresh: Boolean = false) = liveData {
        emit(State.Loading)
        emitSource(repo.getSnippetsWithPreferredTags(sharedPref.user.tags, forceRefresh).asLiveData())
    }

    fun isNewUser() = sharedPref.user.tags.isEmpty()
}