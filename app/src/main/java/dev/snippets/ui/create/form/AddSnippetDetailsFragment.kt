package dev.snippets.ui.create.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.R
import dev.snippets.databinding.FragmentAddSnippetDetailsBinding
import dev.snippets.ui.create.CreateFragment
import dev.snippets.ui.create.CreateViewModel
import dev.snippets.util.*

@AndroidEntryPoint
class AddSnippetDetailsFragment : Fragment() {
    private lateinit var binding: FragmentAddSnippetDetailsBinding
    private val model by activityViewModels<CreateViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddSnippetDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.reset()

        model.tagsList.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    binding.layoutNormal.hide()
                    binding.progressBar.showWithAnimation()
                }
                is State.Error -> {
                    binding.progressBar.hide()
                    binding.root.errorSnackbar(it.message)
                }
                is State.Success -> {
                    binding.progressBar.hide()
                    loadView()
                    binding.layoutNormal.showWithAnimation()
                }
            }
        }
    }

    private fun loadView() {
        (binding.textFieldLanguage.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                Constants.listOfLanguages
            )
        )

        binding.textFieldSnippetTitle.editText?.doOnTextChanged { text, _, _, _ ->
            binding.textFieldSnippetTitle.error = if (text.isNullOrBlank()) {
                "Title is required"
            } else {
                null
            }
            model.title = text.toString()
        }

        inflateChips(layoutInflater, binding.chipGroupTags, (model.tagsList.value as? State.Success)?.data ?: emptyList(), R.layout.layout_filter_chip)

        binding.textFieldSnippetDescription.editText?.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.textFieldSnippetDescription.error = "Description is required"
            } else {
                binding.textFieldSnippetDescription.error = null
                model.description = text.toString()
            }
        }

        binding.textFieldLanguage.editText?.doOnTextChanged { text, _, _, _ ->
            binding.textFieldLanguage.error = if (text.isNullOrEmpty()) {
                "Language is required"
            } else if (!text.toString().isValidLanguageChoice()) {
                "Invalid language choice"
            } else {
                null
            }
            model.language = text.toString()
        }

        binding.textFieldSnippetDescription.editText?.doOnTextChanged { text, _, _, _ ->
            model.description = text.toString()
        }

        binding.buttonNext.setOnClickListener {
            for (chip in binding.chipGroupTags.children) {
                (chip as? Chip)?.let {
                    if (it.isChecked) {
                        model.tags.add(it.text.toString())
                    }
                }
            }

            if (model.canMoveToAddImage()) {
                (parentFragment as? CreateFragment)?.nextPage()
            } else {
                binding.root.errorSnackbar("Please fill out all required fields")
            }
        }
    }
}