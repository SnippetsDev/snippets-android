package dev.snippets.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.R
import dev.snippets.data.models.Snippet
import dev.snippets.databinding.FragmentDetailBinding
import dev.snippets.util.*
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val model by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args by navArgs<DetailFragmentArgs>()
        model.getSnippet(args.snippetId).observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    binding.progressBar.show()
                }
                is State.Error -> {
                    binding.progressBar.hide()
                    binding.root.errorSnackbar(it.message)
                    findNavController().popBackStack()
                }
                is State.Success -> {
                    binding.progressBar.hide()
                    setupSnippet(it.data.snippets[0])
                }
            }
        }
    }

    private fun setupSnippet(snippet: Snippet) {
        with(binding) {
            textViewSnippetTitle.text = snippet.title
            textViewSnippetDescription.text = snippet.description
            imageViewSnippetOutputImage.load(snippet.imageUrl) {
                transformations(RoundedCornersTransformation(16f))
            }
            inflateChips(
                layoutInflater,
                chipGroupSnippetTags,
                snippet.tags,
                R.layout.layout_chip_tag
            )
            codeViewSnippetCode.apply {
                setOptions(Options.Default.get(requireContext()).withTheme(ColorTheme.MONOKAI))
                setCode(snippet.code ?: "An error occurred while loading the code!")
            }
            buttonCopyCode.setOnClickListener {
                requireContext().copyToClipboard("Snippet Code", snippet.code ?: "")
            }
        }
    }
}