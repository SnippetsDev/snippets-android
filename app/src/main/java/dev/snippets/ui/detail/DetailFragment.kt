package dev.snippets.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.snackbar.Snackbar
import dev.snippets.R
import dev.snippets.databinding.FragmentDetailBinding
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // MockSnippet("Double Back Button Press Exit", listOf("Android", "Kotlin"), "Exit Android app on two taps of the back button along with a toast", R.drawable.sample_2),

        with (binding) {
            textViewSnippetTitle.text = "Double Back Button Press Exit"
            textViewSnippetDescription.text = "Exit Android app on two taps of the back button along with a toast"
            with (codeViewSnippetCode) {
                setOptions(Options.Default.get(requireContext()).withTheme(ColorTheme.MONOKAI))
                setCode(
                    """
                    private var isBackPressed = false
                    override fun onBackPressed() {
                        if (!isBackPressed) {
                        // If in fragment, use back pressed dispatcher
                           Toast.makeText(this, "Press again to exit!", Toast.LENGTH_SHORT).show()
                        } else {
                            finish()
                        }
                    }
                """.trimIndent(),
                )
            }

            buttonCopyCode.setOnClickListener {
                Snackbar.make(binding.root, "Code Copied!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}