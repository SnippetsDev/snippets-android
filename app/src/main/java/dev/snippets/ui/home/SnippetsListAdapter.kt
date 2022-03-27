package dev.snippets.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import dev.snippets.R
import dev.snippets.data.Snippet
import dev.snippets.databinding.LayoutSnippetBinding

class SnippetsListAdapter(context: Context, private val snippets: List<Snippet>) :
    RecyclerView.Adapter<SnippetsListAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutSnippetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutSnippetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewSnippetTitle.text = snippets[position].title
        holder.binding.textViewSnippetDescription.text = snippets[position].description
        holder.binding.imageViewSnippetOutput.load(snippets[position].imageUrl) {
            transformations(RoundedCornersTransformation(20f))
        }

        holder.binding.root.setOnClickListener {
            holder.binding.root.findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
        // Chip inflation omitted for now
    }

    override fun getItemCount() = snippets.size
}