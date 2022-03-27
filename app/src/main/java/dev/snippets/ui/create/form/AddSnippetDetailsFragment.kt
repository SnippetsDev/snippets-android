package dev.snippets.ui.create.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.databinding.FragmentAddSnippetDetailsBinding
import dev.snippets.ui.create.CreateFragment
import dev.snippets.ui.create.CreateViewModel
import dev.snippets.util.Constants

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

        (binding.textFieldLanguage.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                Constants.listOfLanguages
            )
        )

        binding.buttonNext.setOnClickListener {
            (parentFragment as CreateFragment).nextPage()
        }
    }
}