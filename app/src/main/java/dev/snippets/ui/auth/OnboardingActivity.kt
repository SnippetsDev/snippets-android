package dev.snippets.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.R
import dev.snippets.databinding.ActivityOnboardingBinding
import dev.snippets.ui.MainActivity
import dev.snippets.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private val model by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.getAllTags().observe(this) {
            when (it) {
                is State.Loading -> {
                    binding.layoutNormal.hide()
                    binding.layoutError.hide()
                    binding.progressBar.showWithAnimation()
                }
                is State.Error -> {
                    binding.layoutNormal.hide()
                    binding.progressBar.hide()
                    binding.layoutError.showWithAnimation()
                    binding.root.errorSnackbar(it.message)
                }
                is State.Success -> {
                    binding.layoutError.hide()
                    binding.progressBar.hide()
                    binding.layoutNormal.showWithAnimation()
                    loadView()
                }
            }
        }
    }

    private fun loadView() {
        lifecycleScope.launch {
            binding.textViewOnboardingIntro.animateText("Hi!")
            delay(3000)

            binding.textViewOnboardingIntro.animateText("Welcome to Snippets!")
            delay(3000)

            binding.textViewOnboardingIntro.animateText(
                "Snippets allows you to share and discover exciting pieces of code " +
                        "that are relevant to YOU."
            )
            delay(6000)

            binding.textViewOnboardingIntro.animateText(
                "To get started, select upto 3 tags from the choices below. You'll start seeing Snippets " +
                        "that other developers have shared containing these tags."
            )

            delay(2500)

            inflateChips(
                layoutInflater,
                binding.chipGroupTags,
                model.listTags,
                R.layout.layout_filter_chip,
                false
            )
            binding.chipGroupTags.showWithAnimation()
            binding.buttonDone.showWithAnimation()
        }

        binding.buttonDone.setOnClickListener {
            model.listTags.clear()
            for (chip in binding.chipGroupTags.children) {
                (chip as Chip).let {
                    if (it.isChecked) {
                        model.listTags.add(it.text.toString())
                    }
                }
            }
            if (model.listTags.size > 3) {
                binding.root.shortSnackbar("Please select a maximum of 3 tags")
                return@setOnClickListener
            } else if (model.listTags.isEmpty()) {
                binding.root.shortSnackbar("Please select at least one tag!")
                return@setOnClickListener
            }
            model.setPreferredTags()
            startActivity(Intent(
                this, MainActivity::class.java
            ).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }
}