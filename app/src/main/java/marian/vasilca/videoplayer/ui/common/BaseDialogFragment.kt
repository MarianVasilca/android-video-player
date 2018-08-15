package marian.vasilca.videoplayer.ui.common

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import marian.vasilca.videoplayer.R

abstract class BaseDialogFragment : AppCompatDialogFragment() {

    protected val onDialogItemSelectedListener
        get() = parentFragment as? OnDialogItemSelectedListener
                ?: activity as? OnDialogItemSelectedListener

    override fun onCreateDialog(savedInstanceState: Bundle?) =
            context?.let {
                AlertDialog.Builder(it, R.style.DialogTheme).createDialog(arguments)
            } ?: super.onCreateDialog(savedInstanceState)

    abstract fun AlertDialog.Builder.createDialog(arguments: Bundle?): AlertDialog

    interface OnDialogItemSelectedListener {
        fun onPositiveButtonSelected(id: Int)
        fun onNegativeButtonSelected(id: Int) = Unit
        fun onNeutralButtonSelected(id: Int) = Unit
    }
}