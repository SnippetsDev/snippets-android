package dev.snippets.util

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.snippets.BuildConfig
import dev.snippets.R
import java.text.SimpleDateFormat
import java.util.*

fun log(vararg messages: String) {
    if (BuildConfig.DEBUG) {
        for (message in messages) {
            Log.d("SnippetsDebug", message)
        }
    }
}

fun View.show() = run { this.visibility = View.VISIBLE }

fun View.hide() = run { this.visibility = View.GONE }

fun View.showWithAnimation() = run {
    this.show()
    this.animate().alpha(1f).setDuration(500).start()
}

fun View.hideWithAnimation() = run {
    this.animate().alpha(0f).setDuration(500).withEndAction { this.hide() }.start()
}

fun View.errorSnackbar(message: String) = run {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setTextColor(Color.WHITE)
        .setBackgroundTint(Color.parseColor("#B71C1C"))
        .show()
}

fun View.shortSnackbar(message: String) = run {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun String.isValidLanguageChoice() = Constants.listOfLanguages.find { it == this } != null

fun getFormattedDateTime(): String = run {
    SimpleDateFormat("dd/MM/yy hh:mm aaa", Locale.ENGLISH).format(System.currentTimeMillis())
}

fun getUuid() = UUID.randomUUID().toString()

fun getUniqueNameForImage() = "${getUuid()} ${getFormattedDateTime()}"

fun View.disable() = run { this.isEnabled = false }

fun View.enable() = run { this.isEnabled = true }