package dev.snippets.ui.detail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.R
import dev.snippets.data.models.Snippet
import dev.snippets.databinding.FragmentDetailBottomSheetBinding
import dev.snippets.util.*
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme

@AndroidEntryPoint
class DetailBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDetailBottomSheetBinding
    private val model by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setOnShowListener {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args by navArgs<DetailBottomSheetFragmentArgs>()
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
                    setupSnippet(it.data)
                }
            }
        }
    }

    private fun setupSnippet(snippet: Snippet) {
        with(binding) {
            textViewSnippetTitle.text = snippet.title
            textViewSnippetDescription.text = snippet.description
            snippet.imageUrl?.let {
                imageViewSnippetOutputImage.load(snippet.imageUrl) {
                    crossfade(true)
                    placeholder(context?.getCircularProgressDrawable())
                    transformations(RoundedCornersTransformation(16f))
                }
                imageViewSnippetOutputImage.setOnClickListener {
                    findNavController().navigate(R.id.action_detailBottomSheetFragment_to_expandedImageActivity, bundleOf("imageUrl" to snippet.imageUrl))
                }
            } ?: imageViewSnippetOutputImage.hide()
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
                binding.root.shortSnackbar("Code copied to clipboard!")
            }
            buttonShareSnippet.setOnClickListener {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Hey! Check out this awesome code snippet: https://snippetsdev.netlify.app/snippet/${snippet.id}")
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Share Snippet"))
            }
        }
    }
}