package dev.snippets.ui.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

}