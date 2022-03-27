package dev.snippets.data

import dev.snippets.util.State
import dev.snippets.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

class Repository (
    private val api: Api
) {
    suspend fun getAllSnippets() = dataOrError { api.getAllSnippets() }

    suspend fun getAllTags() = dataOrError { api.getAllTags() }

    /**
     * This is a really common pattern where the API call result needs to be checked for errors.
     * This method simplifies the error handling and returns the data if successful, error message otherwise
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