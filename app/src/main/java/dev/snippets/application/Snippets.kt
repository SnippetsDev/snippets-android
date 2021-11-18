package dev.snippets.application

import android.app.Application
import io.github.kbiakov.codeview.classifier.CodeProcessor

class Snippets : Application() {
    override fun onCreate() {
        super.onCreate()

        CodeProcessor.init(this)
    }
}