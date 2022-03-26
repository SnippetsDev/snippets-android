package dev.snippets.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.kbiakov.codeview.classifier.CodeProcessor

@HiltAndroidApp
class Snippets : Application() {
    override fun onCreate() {
        super.onCreate()

        CodeProcessor.init(this)
    }
}