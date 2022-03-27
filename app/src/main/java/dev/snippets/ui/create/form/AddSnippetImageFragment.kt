package dev.snippets.ui.create.form

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.databinding.FragmentAddSnippetImageBinding
import dev.snippets.ui.create.CreateFragment
import dev.snippets.ui.create.CreateViewModel
import dev.snippets.util.errorSnackbar
import dev.snippets.util.log

@AndroidEntryPoint
class AddSnippetImageFragment : Fragment() {
    private lateinit var binding: FragmentAddSnippetImageBinding
    private val model by activityViewModels<CreateViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddSnippetImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val startForImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            model.imageUri = data?.data!!.also {
                log("Received uri: $it")
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            binding.root.errorSnackbar(ImagePicker.getError(data))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewPickCamera.setOnClickListener {
            ImagePicker.with(this@AddSnippetImageFragment)
                .cameraOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { startForImageResult.launch(it) }
        }

        binding.imageViewPickGallery.setOnClickListener {
            ImagePicker.with(this@AddSnippetImageFragment)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { startForImageResult.launch(it) }
        }

        binding.buttonNext.setOnClickListener {
            (parentFragment as CreateFragment).nextPage()
        }
    }
}