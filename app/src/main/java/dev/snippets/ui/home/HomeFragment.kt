package dev.snippets.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.data.Snippet
import dev.snippets.databinding.FragmentHomeBinding
import dev.snippets.util.State
import dev.snippets.util.errorSnackbar
import dev.snippets.util.hideWithAnimation
import dev.snippets.util.showWithAnimation

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val model by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.getAllSnippets().observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> binding.progressBar.showWithAnimation()
                is State.Error -> binding.root.errorSnackbar(it.message)
                is State.Success -> {
                    binding.progressBar.hideWithAnimation()
                    binding.listSnippets.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = SnippetsListAdapter(requireContext(), it.data.snippets)
                    }
                }
            }
        }
    }
}