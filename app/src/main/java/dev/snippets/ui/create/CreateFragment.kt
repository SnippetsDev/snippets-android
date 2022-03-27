package dev.snippets.ui.create

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.databinding.FragmentCreateBinding
import dev.snippets.util.Constants
import dev.snippets.util.errorSnackbar
import dev.snippets.util.log

@AndroidEntryPoint
class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding
    private val model by viewModels<CreateViewModel>()

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

        setupViews()
    }

    private fun setupViews() {
        with (binding) {
            buttonInputCode.setOnClickListener {
                InputCodeDialogFragment().show(parentFragmentManager, "INPUT_MODAL")
            }

            imageViewPickCamera.setOnClickListener {
                ImagePicker.with(this@CreateFragment)
                    .cameraOnly()
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent { startForImageResult.launch(it) }
            }

            imageViewPickGallery.setOnClickListener {
                ImagePicker.with(this@CreateFragment)
                    .galleryOnly()
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent { startForImageResult.launch(it) }
            }


            (textFieldLanguage.editText as? AutoCompleteTextView)?.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    Constants.listOfLanguages
                )
            )
        }
    }

    private val startForImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            model.imageUri = data?.data!!
            log("Received uri: ${model.imageUri}")
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            binding.root.errorSnackbar(ImagePicker.getError(data))
        }
    }
}