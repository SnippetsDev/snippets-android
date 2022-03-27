package dev.snippets.ui.create

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    var uploading = MutableLiveData(false)

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

    fun uploadImageToFirebase() = if (imageUri != null)
        repo.uploadImageToFirebase(imageUri!!) else throw IllegalStateException("Image Uri is null")
}