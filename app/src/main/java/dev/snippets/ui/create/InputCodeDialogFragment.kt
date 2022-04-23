package dev.snippets.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.snippets.databinding.FragmentInputCodeDialogBinding
import dev.snippets.util.Constants

class InputCodeDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentInputCodeDialogBinding

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        binding = FragmentInputCodeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonDone.setOnClickListener {
            if (binding.textFieldInputCode.editText?.text.isNullOrEmpty()) {
                binding.textFieldInputCode.error = "Code cannot be empty"
            } else {
                setFragmentResult(Constants.KEY_INPUT_CODE_DIALOG, Bundle().apply {
                    putString(
                        Constants.KEY_CODE,
                        binding.textFieldInputCode.editText?.text.toString()
                    )
                })
                dismiss()
            }
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }
}