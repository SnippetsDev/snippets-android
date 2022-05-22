package dev.snippets.ui.home

import android.content.Context
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.chip.Chip
import dev.snippets.R
import dev.snippets.data.models.Snippet
import dev.snippets.databinding.LayoutSnippetBinding
import dev.snippets.util.getCircularProgressDrawable
import dev.snippets.util.hide

class SnippetsListAdapter(private val context: Context, private val snippets: List<Snippet>) :
    RecyclerView.Adapter<SnippetsListAdapter.ViewHolder>() {

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