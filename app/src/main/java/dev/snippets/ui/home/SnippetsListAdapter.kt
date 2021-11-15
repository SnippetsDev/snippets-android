package dev.snippets.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.snippets.R
import dev.snippets.databinding.LayoutSnippetBinding

class SnippetsListAdapter(context: Context, private val dataset: List<String>) :
    RecyclerView.Adapter<SnippetsListAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutSnippetBinding) : RecyclerView.ViewHolder(binding.root) {
        var liked = false
        var bookmarked = false

        init {
            binding.imageViewLike.setOnClickListener {
                if (liked) {
                    liked = false
                    binding.imageViewLike.setImageResource(R.drawable.outline_favorite_border_24)
                } else {
                    liked = true
                    binding.imageViewLike.setImageResource(R.drawable.outline_favorite_24)
                }
            }

            binding.imageViewBookmark.setOnClickListener {
                if (bookmarked) {
                    bookmarked = false
                    binding.imageViewBookmark.setImageResource(R.drawable.outline_collections_bookmark_24)
                } else {
                    bookmarked = true
                    binding.imageViewBookmark.setImageResource(R.drawable.baseline_collections_bookmark_24)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutSnippetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewSnippetTitle.text = dataset[position]
    }

    override fun getItemCount() = dataset.size
}