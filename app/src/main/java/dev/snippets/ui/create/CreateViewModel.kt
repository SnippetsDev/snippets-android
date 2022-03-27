package dev.snippets.ui.create

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    var title = ""
    var imageUri = "".toUri()
    var code = ""
    var tags = ""
    var description = ""
    var language = ""

    init {
        reset()
    }

    fun reset() {
        title = ""
        imageUri = "".toUri()
        code = ""
        tags = ""
        description = ""
        language = ""

    }

    fun publishSnippet() {

    }
}