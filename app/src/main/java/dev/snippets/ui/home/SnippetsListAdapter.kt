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
import dev.snippets.databinding.LayoutSnippetBinding

class SnippetsListAdapter(context: Context, private val dataset: List<String>) :
    RecyclerView.Adapter<SnippetsListAdapter.ViewHolder>() {
    private var snippets: List<MockSnippet> = listOf(
        MockSnippet("On Exit Bottom Sheet Dialog", listOf("Android", "Kotlin"), "Code to pop up a confirmation bottom dialog on app exit", R.drawable.sample_1),
        MockSnippet("Double Back Button Press Exit", listOf("Android", "Kotlin"), "Exit Android app on two taps of the back button along with a toast", R.drawable.sample_2),
        MockSnippet("Basic RecyclerView with Single TextView", listOf("Android", "Kotlin"), "Display a simple RecyclerView of text in your app", R.drawable.sample)
    )

    init {
        // Create Mocking Data
    }

    class ViewHolder(val binding: LayoutSnippetBinding) : RecyclerView.ViewHolder(binding.root) {
        private var liked = false
        private var bookmarked = false

        init {
            binding.buttonLike.setOnClickListener {
                liked = !liked
            }

            binding.buttonBookmark.setOnClickListener {
                bookmarked = !bookmarked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutSnippetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewSnippetTitle.text = snippets[position].title
        holder.binding.textViewSnippetDescription.text = snippets[position].description
        holder.binding.imageViewSnippetOutput.load(snippets[position].imageUri) {
            transformations(RoundedCornersTransformation(20f))
        }

        holder.binding.root.setOnClickListener {
            holder.binding.root.findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
        // Chip inflation omitted for now
    }

    override fun getItemCount() = dataset.size
}

data class MockSnippet(
    val title: String,
    val tags: List<String>,
    val description: String,
    @DrawableRes val imageUri: Int
)