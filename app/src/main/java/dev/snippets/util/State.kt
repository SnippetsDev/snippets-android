package dev.snippets.util

/**
 * Represents the state of the UI at a given point in time.
 */
sealed class State<out T> {
    object Loading : State<Nothing>()
    class Success<T>(val data: T) : State<T>()
    class Error(val message: String) : State<Nothing>()
}
