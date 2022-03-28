package dev.snippets.data

import android.net.Uri
import androidx.lifecycle.liveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.snippets.util.State
import dev.snippets.util.getUniqueNameForImage
import dev.snippets.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class Repository (
    private val api: Api
) {
    suspend fun getSnippetsWithPreferredTags(tags: String) = dataOrError { api.getSnippetsForTags(tags) }

    suspend fun getAllSnippets() = dataOrError { api.getAllSnippets() }

    suspend fun getAllTags() = dataOrError { api.getAllTags() }

    suspend fun getSnippet(id: String) = dataOrError { api.getSnippet(id) }

    fun uploadImageToFirebase(imageUri: Uri) = liveData {
        emit(State.Loading)
        val ref = Firebase.storage.reference.child("snippet_outputs/${getUniqueNameForImage()}")
        val uploadTask = ref.putFile(imageUri)
        try {
            val result = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.await()
            emit(State.Success(result.toString()))
        } catch (e: Exception) {
            emit(State.Error("Failed to upload image, ${e.message}"))
        }
    }

    fun publishSnippet(snippet: Snippet) = liveData {
        emit(State.Loading)
        val state = dataOrError {
            api.publishSnippet(snippet)
        }
        emit(state)
    }


    /**
     * This is a really common pattern where the API call result needs to be checked for errors.
     * This method simplifies the error handling and returns the data if successful, error message otherwise.
     * Note that this method returns the data or error wrapped in a State object. It does not emit a loading state.
     */
    private suspend fun <T> dataOrError(apiCall: suspend () -> Response<T>): State<T> = withContext(Dispatchers.IO) {
        try {
            val response = apiCall()
            when {
                response.code() != 200 -> {
                    State.Error("Received error code ${response.code()}, ${response.message()}")
                }
                else -> {
                    State.Success(response.body()!!)
                }
            }
        } catch (exception: Exception) {
            log("Caught exception while making API call, Exception: $exception, Message: ${exception.message}")
            if (exception !is HttpException) {
//                Firebase.crashlytics.recordException(exception)
            }
            val errorPrompt = exception.message ?: "Something went wrong"
            State.Error(errorPrompt)
        }
    }
}