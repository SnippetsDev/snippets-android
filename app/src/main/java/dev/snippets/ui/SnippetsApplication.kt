package dev.snippets.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import io.github.kbiakov.codeview.classifier.CodeProcessor

@HiltAndroidApp
class SnippetsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        CodeProcessor.init(this)
    }
}