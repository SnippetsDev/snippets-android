package dev.snippets.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.flexbox.*
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.BuildConfig
import dev.snippets.data.SharedPrefHelper
import dev.snippets.databinding.FragmentUserBinding
import dev.snippets.databinding.ListItemTagBinding
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    @Inject
    lateinit var sharedPref: SharedPrefHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            val user = sharedPref.user
            textViewUserLoginName.text = "@${user.username}"
            textViewUserName.text = user.name
            textViewUserEmail.text = user.email
            textViewUserBio.text = user.bio
            textViewVersionName.text = "Version ${BuildConfig.VERSION_NAME}"
            imageViewUserImage.load(user.imageUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            listTags.adapter = TagsListAdapter(user.tags)
            FlexboxLayoutManager(requireContext()).apply {
                justifyContent = JustifyContent.CENTER
                alignItems = AlignItems.CENTER
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                listTags.layoutManager = this
            }
        }
    }
}

class TagsListAdapter(private val tags: List<String>) : RecyclerView.Adapter<TagsListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListItemTagBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.chipTag.text = tags[position]
    }

    override fun getItemCount() = tags.size
}