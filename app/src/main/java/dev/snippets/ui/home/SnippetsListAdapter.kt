package dev.snippets.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.snippets.databinding.LayoutSnippetBinding

class SnippetsListAdapter(context: Context, private val dataset: List<String>) :
    RecyclerView.Adapter<SnippetsListAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutSnippetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutSnippetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewSnippetTitle.text = dataset[position]
    }

    override fun getItemCount() = dataset.size
}