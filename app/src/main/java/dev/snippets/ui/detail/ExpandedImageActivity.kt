package dev.snippets.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import coil.load
import dev.snippets.R
import dev.snippets.databinding.ActivityExpandedImageBinding
import dev.snippets.util.getCircularProgressDrawable

class ExpandedImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpandedImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpandedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.extras?.getString("imageUrl")?.let {
            binding.imageView.load(it) {
                crossfade(true)
                placeholder(getCircularProgressDrawable())
            }
        }
    }
}