package dev.snippets.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import dev.snippets.util.State
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: Repository,
) : ViewModel() {

    fun getSnippet(id: String) = liveData {
        emit(State.Loading)
        emitSource(repo.getSnippet(id).asLiveData())
    }
}