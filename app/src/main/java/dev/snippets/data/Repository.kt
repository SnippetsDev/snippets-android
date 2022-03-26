package dev.snippets.data

import dev.snippets.util.State
import dev.snippets.util.log
import retrofit2.HttpException
import retrofit2.Response

class Repository (
    private val api: Api
) {
    suspend fun getAllSnippets() {
        log(api.getAllSnippets().body().toString())
    }

    /**
     * Takes in an api call and returns an appropriate UI state based on the response
     */
    private suspend fun <T> dataOrError(apiCall: suspend () -> Response<T>): State<T> {
        try {
            val response = apiCall()
            return when {
                response.code() != 200 -> {
                    State.Error(response.message())
                }
                else -> {
                    State.Success(response.body()!!)
                }
            }
        } catch (exception: Exception) {
            log("Caught exception while making API call, Exception: $exception, Message: ${exception.message}")
            val errorPrompt = if (exception is HttpException) {
                exception.message ?: "Something went wrong"
            } else {
//                Firebase.crashlytics.recordException(exception)
                "Something went wrong"
            }
            return State.Error(errorPrompt)
        }
    }
}