package dev.snippets.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    fun getAllSnippets() {
        viewModelScope.launch {
            repo.getAllSnippets()
        }
    }
}