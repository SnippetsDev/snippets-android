package dev.snippets.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.BuildConfig
import dev.snippets.databinding.ActivityAuthBinding
import dev.snippets.ui.MainActivity
import dev.snippets.util.*

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardViewLoginWithGithub.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://github.com/login/oauth/authorize?client_id=${BuildConfig.GITHUB_CLIENT_ID}".toUri()
                ).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.data?.getQueryParameter("code") != null) {
            viewModel.loginWithGithub(intent.data?.getQueryParameter("code")!!).observe(this) {
                when (it) {
                    is State.Loading -> {
                        binding.cardViewLoginWithGithub.hide()
                        binding.lottieLogin.hide()
                        binding.lottieLoading.showWithAnimation()
                    }
                    is State.Error -> {
                        binding.root.shortSnackbar("Login failed due to ${it.message}")
                        binding.lottieLoading.hide()
                        binding.cardViewLoginWithGithub.showWithAnimation()
                        binding.lottieLogin.showWithAnimation()
                    }
                    is State.Success -> {
                        if (viewModel.isNewUser()) startActivity(
                            Intent(
                                this,
                                OnboardingActivity::class.java
                            )
                        )
                        else clearBackStackAndLaunchActivity(MainActivity::class.java)
                    }
                }
            }
        } else {
            binding.root.shortSnackbar("Could not retrieve token from GitHub, login failed")
        }
    }
}