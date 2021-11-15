package dev.snippets.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import dev.snippets.R
import dev.snippets.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            imageViewUserImage.load(R.drawable.sample_user_image) {
                transformations(CircleCropTransformation())
            }

            textViewUserName.text = "Guido van Rossum"
            textViewUserEmail.text = "guido@python.org"
            textViewUserBio.text = "Creator of the Python programming language. " +
                    "Holds a Master's degree in Computer Science and Mathematic. " +
                    "Previously Senior Staff Engineer @ Google, Principal Engineer @ Dropbox. " +
                    "Currently Distinguished Engineer @ Microsoft."
        }
    }
}