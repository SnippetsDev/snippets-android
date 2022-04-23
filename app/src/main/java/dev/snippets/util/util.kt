package dev.snippets.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import dev.snippets.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

fun log(vararg messages: String?) {
    if (BuildConfig.DEBUG) {
        for (message in messages) {
            if (message != null) Log.d("SnippetsDebug", message)
        }
    }
}

fun View.show() = run { this.visibility = View.VISIBLE }

fun View.hide() = run { this.visibility = View.GONE }

fun View.showWithAnimation() = run {
    this.apply {
        alpha = 0f
        visibility = View.VISIBLE
        animate().alpha(1f).duration = 500.toLong()
    }
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

fun inflateChips(
    inflater: LayoutInflater,
    chipGroup: ChipGroup,
    items: List<String>,
    layout: Int,
    compress: Boolean = true
) {
    for (item in items) {
        val chip = inflater.inflate(layout, chipGroup, false) as Chip
        chip.text = item
        chip.setEnsureMinTouchTargetSize(compress)
        chipGroup.addView(chip)
    }
}

fun Context.copyToClipboard(clipLabel: String, text: CharSequence) {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText(clipLabel, text))
}

// Converts a list of Strings to a String with each item separated by a comma
fun List<String>.toStringWithCommas(): String = this.joinToString(",")

// Converts comma separated String to a list of Strings
fun String.toListOfStrings(): List<String> = this.split(",")

fun Context.clearBackStackAndLaunchActivity(activity: Class<*>) {
    val intent = Intent(this, activity)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}