package dev.snippets.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.R
import dev.snippets.databinding.FragmentHomeBinding
import dev.snippets.ui.auth.AuthActivity
import dev.snippets.util.*

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

        if (model.isNewUser()) requireContext().clearBackStackAndLaunchActivity(AuthActivity::class.java)

        binding.listSnippets.setLayoutManager(LinearLayoutManager(context))

        binding.layoutSwipeRefresh.setOnRefreshListener {
            renderSnippets(forceRefresh = true)
        }
        renderSnippets()
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(Constants.KEY_PUBLISHED_SNIPPET) { requestKey, bundle ->
            if (bundle[Constants.KEY_PUBLISHED_SNIPPET] != null) {
                renderSnippets(forceRefresh = true)
            }
        }
    }

    /**
     * Had to separate this out because if the livedata emitted from the viewmodel
     * is not observed, it'll not work at all. This was causing problems with the swipe to refresh
     * feature, so this fixes it.
     */
    private fun renderSnippets(forceRefresh: Boolean = false) {
        model.getSnippetsWithPreferredTags(forceRefresh).observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    binding.lottie.hide()
                    binding.listSnippets.showWithAnimation()
                    binding.listSnippets.apply {
                        addVeiledItems(15)
                        veil()
                    }
                }
                is State.Error -> {
                    binding.layoutSwipeRefresh.isRefreshing = false
                    binding.root.errorSnackbar(it.message)
                    binding.lottie.setAnimation(R.raw.error)
                    binding.listSnippets.hide()
                    binding.lottie.showWithAnimation()
                }
                is State.Success -> {
                    binding.layoutSwipeRefresh.isRefreshing = false
                    if (it.data.isEmpty()) {
                        binding.listSnippets.hideWithAnimation()
                        binding.lottie.setAnimation(R.raw.empty)
                        binding.lottie.showWithAnimation()
                    } else {
                        binding.lottie.hide()
                        binding.listSnippets.unVeil()
                        binding.listSnippets.getRecyclerView().apply {
                            adapter = SnippetsListAdapter(requireContext(), it.data)
                        }
                        binding.listSnippets.showWithAnimation()
                    }
                }
            }
        }
    }
}