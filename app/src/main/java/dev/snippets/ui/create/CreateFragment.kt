package dev.snippets.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.databinding.FragmentCreateBinding
import dev.snippets.ui.create.form.AddSnippetCodeFragment
import dev.snippets.ui.create.form.AddSnippetDetailsFragment
import dev.snippets.ui.create.form.AddSnippetImageFragment
import dev.snippets.util.DepthPageTransformer

@AndroidEntryPoint
class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding
    private val model by activityViewModels<CreateViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.formPager.adapter = ScreenSlidePagerAdapter(this)
        binding.formPager.isUserInputEnabled = false
        binding.formPager.setPageTransformer(DepthPageTransformer())
    }

    fun nextPage() {
        binding.formPager.currentItem = binding.formPager.currentItem + 1
    }
}

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
private class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> AddSnippetDetailsFragment()
        1 -> AddSnippetImageFragment()
        2 -> AddSnippetCodeFragment()
        else -> {
            throw IllegalArgumentException("Invalid position")
        }
    }
}