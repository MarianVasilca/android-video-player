package marian.vasilca.videoplayer.ui.common

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog

class AlertDialogFragment : BaseDialogFragment() {

    companion object {
        private const val ID_TAG = "id"
        private const val TITLE_TAG = "title"
        private const val MESSAGE_TAG = "message"
        private const val POSITIVE_BUTTON_TAG = "positiveButton"
        private const val NEGATIVE_BUTTON_TAG = "negativeButton"
        private const val NEUTRAL_BUTTON_TAG = "neutralButton"

        fun create(
                id: Int,
                @StringRes title: Int,
                @StringRes message: Int,
                @StringRes positiveButton: Int,
                @StringRes negativeButton: Int,
                @StringRes neutralButton: Int = 0
        ): AlertDialogFragment {
            val bundle = Bundle()
            bundle.putInt(ID_TAG, id)
            bundle.putInt(TITLE_TAG, title)
            bundle.putInt(MESSAGE_TAG, message)
            bundle.putInt(POSITIVE_BUTTON_TAG, positiveButton)
            bundle.putInt(NEGATIVE_BUTTON_TAG, negativeButton)
            bundle.putInt(NEUTRAL_BUTTON_TAG, neutralButton)

            val alertDialogFragment = AlertDialogFragment()
            alertDialogFragment.arguments = bundle
            return alertDialogFragment
        }
    }

    override fun AlertDialog.Builder.createDialog(arguments: Bundle?): AlertDialog {
        val id = arguments!!.getInt(ID_TAG)
        val titleId = arguments.getInt(TITLE_TAG)
        val messageId = arguments.getInt(MESSAGE_TAG)
        val positiveMessageId = arguments.getInt(POSITIVE_BUTTON_TAG)
        val negativeMessageId = arguments.getInt(NEGATIVE_BUTTON_TAG)
        val neutralMessageId = arguments.getInt(NEUTRAL_BUTTON_TAG)
        val dialogBuilder = setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(positiveMessageId) { _, _ -> onDialogItemSelectedListener?.onPositiveButtonSelected(id) }
                .setNegativeButton(negativeMessageId) { _, _ -> onDialogItemSelectedListener?.onNegativeButtonSelected(id) }
        if (neutralMessageId != 0) {
            dialogBuilder.setNeutralButton(neutralMessageId) { _, _ -> onDialogItemSelectedListener?.onNeutralButtonSelected(id) }
        }
        return dialogBuilder.create()
    }
}