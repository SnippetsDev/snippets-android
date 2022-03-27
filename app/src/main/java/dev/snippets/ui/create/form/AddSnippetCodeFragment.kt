package dev.snippets.ui.create.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.modernstorage.permissions.RequestAccess
import com.google.modernstorage.permissions.StoragePermissions
import com.google.modernstorage.storage.AndroidFileSystem
import com.google.modernstorage.storage.toOkioPath
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.databinding.FragmentAddSnippetCodeBinding
import dev.snippets.ui.create.CreateViewModel
import dev.snippets.ui.create.InputCodeDialogFragment
import dev.snippets.util.errorSnackbar
import dev.snippets.util.log
import okio.buffer

@AndroidEntryPoint
class AddSnippetCodeFragment : Fragment() {
    private lateinit var binding: FragmentAddSnippetCodeBinding
    private val model by activityViewModels<CreateViewModel>()


    private lateinit var fileSystem: AndroidFileSystem

    private val requestFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            model.code = fileSystem.source(uri.toOkioPath()).buffer().readUtf8().also {
                log("Received code: $it")
            }
        } else {
            binding.root.errorSnackbar("Failed to open file")
        }
    }

    private val requestStorageAccess = registerForActivityResult(RequestAccess()) { hasAccess ->
        if (!hasAccess) {
            requestFile.launch(arrayOf("text/*"))
        } else {
            binding.root.errorSnackbar("Can't access files without storage permissions!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddSnippetCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileSystem = AndroidFileSystem(requireContext())


        binding.buttonInputCode.setOnClickListener {
            InputCodeDialogFragment().show(parentFragmentManager, "INPUT_MODAL")
        }

        binding.buttonSelectFileFromDevice.setOnClickListener {
            requestStorageAccess.launch(
                RequestAccess.Args(
                    action = StoragePermissions.Action.READ,
                    types = listOf(StoragePermissions.FileType.Document),
                    createdBy = StoragePermissions.CreatedBy.AllApps
                )
            )
        }

        binding.buttonPublishSnippet.setOnClickListener {
            model.publishSnippet()
        }
    }
}