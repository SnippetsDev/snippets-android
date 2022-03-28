package dev.snippets.ui.create.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.modernstorage.permissions.RequestAccess
import com.google.modernstorage.permissions.StoragePermissions
import com.google.modernstorage.storage.AndroidFileSystem
import com.google.modernstorage.storage.toOkioPath
import dagger.hilt.android.AndroidEntryPoint
import dev.snippets.databinding.FragmentAddSnippetCodeBinding
import dev.snippets.ui.create.CreateViewModel
import dev.snippets.ui.create.InputCodeDialogFragment
import dev.snippets.util.*
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import okio.buffer
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AddSnippetCodeFragment : Fragment() {
    private lateinit var binding: FragmentAddSnippetCodeBinding
    private val model by activityViewModels<CreateViewModel>()


    private lateinit var fileSystem: AndroidFileSystem

    private val requestFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            model.code = fileSystem.source(uri.toOkioPath()).buffer().readUtf8().also {
                log("Received code: $it")
                binding.codeViewSnippetCode.apply {
                    setOptions(Options.Default.get(requireContext()).withTheme(ColorTheme.MONOKAI))
                    setCode(it)
                }
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

        setFragmentResultListener(Constants.KEY_INPUT_CODE_DIALOG) { _, bundle ->
            val code = bundle.getString(Constants.KEY_CODE)
            model.code = code!!
            binding.codeViewSnippetCode.apply {
                setOptions(Options.Default.get(requireContext()).withTheme(ColorTheme.MONOKAI))
                setCode(code)
            }
        }

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
            if (model.code.isNotEmpty()) {
                model.publishSnippet().observe(viewLifecycleOwner) {
                    when (it) {
                        is State.Loading -> binding.progressBar.showWithAnimation()
                        is State.Error -> {
                            binding.progressBar.hideWithAnimation()
                            binding.root.errorSnackbar(it.message)
                        }
                        is State.Success -> {
                            binding.buttonPublishSnippet.disable()
                            binding.progressBar.hideWithAnimation()
                            binding.root.shortSnackbar("Snippet published!")
                            if (model.publishedFirstSnippet()) {
                                binding.konfetti.start(Party(
                                    emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(30)
                                ))
                            }
                            lifecycleScope.launch {
                                delay(3000)
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            } else {
                binding.root.errorSnackbar("A snippet isn't much useful without code!")
            }
        }
    }
}