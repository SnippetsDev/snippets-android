package dev.snippets.util

import android.graphics.Color
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import dev.snippets.BuildConfig

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
        .setBackgroundTint(Color.parseColor("#B71C1C"))
        .show()
}