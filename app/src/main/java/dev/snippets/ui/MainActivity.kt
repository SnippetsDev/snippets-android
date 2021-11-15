package dev.snippets.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.snippets.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}