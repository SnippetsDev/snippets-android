package dev.snippets.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.chip.Chip
import dev.snippets.R
import dev.snippets.data.models.Snippet
import dev.snippets.databinding.LayoutSnippetBinding

class SnippetsListAdapter(private val context: Context, private val snippets: List<Snippet>) :
    RecyclerView.Adapter<SnippetsListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutSnippetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(snippet: Snippet) {
            with(binding) {
                textViewSnippetTitle.text = snippet.title
                textViewSnippetDescription.text = snippet.description
                imageViewSnippetOutput.load(snippet.imageUrl) {
                    transformations(RoundedCornersTransformation(20f))
                }
                for (tag in snippet.tags) {
                    val chip = LayoutInflater.from(context)
                        .inflate(R.layout.layout_chip_tag, chipGroupSnippetTags, false) as Chip
                    chip.text = tag
                    chipGroupSnippetTags.addView(chip)
                }
                root.setOnClickListener {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToDetailBottomSheetFragment(snippet.id)
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutSnippetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(snippets[position])
    }

    override fun getItemCount() = snippets.size
}