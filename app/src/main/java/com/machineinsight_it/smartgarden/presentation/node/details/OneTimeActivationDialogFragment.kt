package com.machineinsight_it.smartgarden.presentation.node.details

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.DialogFragmentOneTimeActivationBinding

class OneTimeActivationDialogFragment : DialogFragment() {
    private lateinit var binding: DialogFragmentOneTimeActivationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.clearFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_fragment_one_time_activation,
            container,
            false
        )

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.add.setOnClickListener {
            try {
                val water = Integer.parseInt(binding.waterEdit.text?.toString() ?: "0")
                parentFragment?.let {
                    if (it is NodeDetailsController) {
                        it.executeOneTimeActivation(water)
                    }
                    dismiss()
                }
            } catch (e: Exception) {
                // no-op
            }
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return dialog
    }
}