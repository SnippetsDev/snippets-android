package dev.snippets.ui.create

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import dev.snippets.util.Constants
import io.github.kbiakov.codeview.Const
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    var title = ""
    var imageUri: Uri? = null
    var code = ""
    var tags = ""
    var description = ""
    var language = ""

    var uploading = MutableLiveData<Boolean>()

    init {
        reset()
    }

    fun reset() {
        title = ""
        imageUri = null
        code = ""
        tags = ""
        description = ""
        language = ""

    }

    fun canMoveToAddImage() = title.isNotEmpty() && tags.isNotEmpty() && language.isNotEmpty()

    fun publishSnippet() {

    }
}