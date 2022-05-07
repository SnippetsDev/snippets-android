package dev.snippets.ui.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.snippets.data.Repository
import dev.snippets.data.SharedPrefHelper
import dev.snippets.data.models.Snippet
import dev.snippets.util.State
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val repo: Repository,
    private val sharedPref: SharedPrefHelper
) : ViewModel() {

    var title = ""
    var imageUri: Uri? = null
    var code = ""
    var tags = mutableListOf<String>()
    var description = ""
    var language = ""

    var listTags = listOf<String>()

    fun getTags() = liveData {
        emit(State.Loading)
        val response = repo.getAllTags()
        if (response is State.Success) {
            listTags = response.data.tags
            emit(State.Success(response.data))
        } else if (response is State.Error) {
            emit(State.Error(response.message))
        }
    }

    fun reset() {
        title = ""
        imageUri = null
        code = ""
        tags = mutableListOf()
        description = ""
        language = ""
    }

    fun canMoveToAddImage() = title.isNotEmpty() && tags.isNotEmpty() && language.isNotEmpty()

    fun publishSnippet() = repo.publishSnippet(
        Snippet(
            "",
            title,
            language,
            description,
            tags,
            imageUri.toString(),
            code,
            0
        )
    )

    fun uploadImageToFirebase() = if (imageUri != null)
        repo.uploadImageToFirebase(imageUri!!) else throw IllegalStateException("Image Uri is null")

    fun publishedFirstSnippet() = sharedPref.publishedFirstSnippet()
}