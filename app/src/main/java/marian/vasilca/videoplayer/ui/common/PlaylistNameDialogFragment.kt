package marian.vasilca.videoplayer.ui.common

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.widget.EditText
import marian.vasilca.videoplayer.R

class PlaylistNameDialogFragment : AppCompatDialogFragment() {

    companion object {
        private const val ID_TAG = "id"
        private const val TITLE_TAG = "title"
        private const val POSITIVE_BUTTON_TAG = "positiveButton"
        private const val NEGATIVE_BUTTON_TAG = "negativeButton"

        fun create(
                id: Int,
                @StringRes title: Int,
                @StringRes positiveButton: Int,
                @StringRes negativeButton: Int
        ): PlaylistNameDialogFragment {
            val bundle = Bundle()
            bundle.putInt(ID_TAG, id)
            bundle.putInt(TITLE_TAG, title)
            bundle.putInt(POSITIVE_BUTTON_TAG, positiveButton)
            bundle.putInt(NEGATIVE_BUTTON_TAG, negativeButton)

            val playlistNameDialogFragment = PlaylistNameDialogFragment()
            playlistNameDialogFragment.arguments = bundle
            return playlistNameDialogFragment
        }
    }

    private val onDialogItemSelectedListener
        get() = parentFragment as? OnDialogItemSelectedListener
                ?: activity as? OnDialogItemSelectedListener

    override fun onCreateDialog(bundle: Bundle?): Dialog {
        val id = arguments!!.getInt(ID_TAG)
        val titleId = arguments!!.getInt(TITLE_TAG)
        val positiveMessageId = arguments!!.getInt(POSITIVE_BUTTON_TAG)
        val negativeMessageId = arguments!!.getInt(NEGATIVE_BUTTON_TAG)

        val viewInflated = requireActivity().layoutInflater.inflate(R.layout.dialog_create_playlist, null)
        val etPlaylistName = viewInflated.findViewById<EditText>(R.id.etPlaylistName)

        val builder = AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setView(viewInflated)
                .setTitle(titleId)
                .setPositiveButton(positiveMessageId) { _, _ -> onDialogItemSelectedListener?.onPositiveButton(id, etPlaylistName.text.toString()) }
                .setNegativeButton(negativeMessageId) { _, _ -> onDialogItemSelectedListener?.onNegativeButton(id) }

        return builder.create()
    }

    interface OnDialogItemSelectedListener {
        fun onPositiveButton(id: Int, playlistName: String)
        fun onNegativeButton(id: Int) = Unit
    }

}
