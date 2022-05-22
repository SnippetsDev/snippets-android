package dev.snippets.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.chip.Chip
import dev.snippets.R
import dev.snippets.data.models.Snippet
import dev.snippets.databinding.LayoutSnippetBinding
import dev.snippets.ui.user.TagsListAdapter
import dev.snippets.util.getCircularProgressDrawable
import dev.snippets.util.hide

class SnippetsListAdapter(private val context: Context) : ListAdapter<Snippet, SnippetsListAdapter.ViewHolder>(SnippetsDiff()) {
    inner class ViewHolder(val binding: LayoutSnippetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(snippet: Snippet) {
            with(binding) {
                textViewSnippetTitle.text = snippet.title
                textViewSnippetDescription.text = snippet.description

                if (snippet.imageUrl.isNullOrEmpty()) imageViewSnippetOutput.hide()
                else imageViewSnippetOutput.load(snippet.imageUrl) {
                    error(R.mipmap.ic_launcher)
                    crossfade(true)
                    placeholder(context.getCircularProgressDrawable())
                    transformations(RoundedCornersTransformation(20f))
                }
                binding.chipGroupSnippetTags.removeAllViews()
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
        holder.bind(getItem(position))
    }
}

class SnippetsDiff : DiffUtil.ItemCallback<Snippet>(

) {
    override fun areItemsTheSame(oldItem: Snippet, newItem: Snippet): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Snippet, newItem: Snippet): Boolean {
        return oldItem == newItem
    }
}