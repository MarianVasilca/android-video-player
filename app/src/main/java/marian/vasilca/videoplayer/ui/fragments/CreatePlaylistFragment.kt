package marian.vasilca.videoplayer.ui.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import kotlinx.android.synthetic.main.fragment_local_files.*
import marian.vasilca.videoplayer.R
import marian.vasilca.videoplayer.data.model.Playlist
import marian.vasilca.videoplayer.data.model.VideoFile
import marian.vasilca.videoplayer.di.InjectorUtils
import marian.vasilca.videoplayer.ui.adapters.videofiles.VideoFileDetailLookup
import marian.vasilca.videoplayer.ui.adapters.videofiles.VideoFileKeyProvider
import marian.vasilca.videoplayer.ui.common.PlaylistNameDialogFragment
import marian.vasilca.videoplayer.viewmodels.playlist.PlaylistViewModel
import java.util.*

/**
 * A fragment where you can create a [Playlist].
 *
 */
class CreatePlaylistFragment : LocalFilesFragment(),
        PlaylistNameDialogFragment.OnDialogItemSelectedListener {

    override val tag: String
        get() = "CreatePlaylistFragment"

    private lateinit var selectedItemCountMenuItem: MenuItem
    private var selectionTracker: SelectionTracker<VideoFile>? = null

    private lateinit var playlistViewModel: PlaylistViewModel

    override fun onBoundViews(savedInstanceState: Bundle?) {
        val factory = InjectorUtils.providePlaylistViewModelFactory(
                requireContext().applicationContext)
        playlistViewModel = ViewModelProviders.of(this, factory).get(PlaylistViewModel::class.java)

        super.onBoundViews(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateSelectionTracker(videoFiles: ArrayList<VideoFile>) {
        selectionTracker = SelectionTracker.Builder<VideoFile>(
                SELECTION_TRACKER_ID,
                rvLocalFiles,
                VideoFileKeyProvider(ItemKeyProvider.SCOPE_CACHED, videoFiles),
                VideoFileDetailLookup(rvLocalFiles),
                StorageStrategy.createParcelableStorage(VideoFile::class.java)
        ).build()

        adapter.setSelectionTracker(selectionTracker)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.local_files_menu, menu)
        selectedItemCountMenuItem = menu!!.findItem(R.id.action_item_count)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create -> {
                showPlaylistNameDialog()
            }
            R.id.action_clear -> {
                selectionTracker?.clearSelection()
            }
        }
        return true
    }

    private fun showPlaylistNameDialog() {
        val playlistNameDialogFragment = PlaylistNameDialogFragment.create(
                id = PLAYLIST_NAME_CODE,
                title = R.string.create_playlist_title,
                positiveButton = R.string.create,
                negativeButton = R.string.cancel
        )
        playlistNameDialogFragment.show(childFragmentManager, PLAYLIST_NAME_DIALOG_TAG)
    }

    override fun onPositiveButton(id: Int, playlistName: String) {
        when (id) {
            PLAYLIST_NAME_CODE -> checkPlaylistError(playlistName)
        }
    }

    private fun checkPlaylistError(playlistName: String) {
        when {
            playlistName.isBlank() -> showErrorMessage(R.string.error_no_title)
            selectionTracker == null -> showErrorMessage(R.string.error_no_selection_tracker)
            selectionTracker!!.selection.isEmpty -> showErrorMessage(R.string.error_no_item_selected)
            else -> createPlaylist(playlistName)
        }
    }

    private fun createPlaylist(playlistName: String) {
        val list = selectionTracker?.selection?.toList() ?: ArrayList()
        Log.i(tag, "Create playlist $playlistName with ${list.size} files")

        playlistViewModel.createPlaylist(Playlist(playlistName, list, Calendar.getInstance().timeInMillis))
        selectionTracker?.clearSelection()
    }

    private fun showErrorMessage(@StringRes id: Int) =
            Snackbar.make(view!!, id, Snackbar.LENGTH_LONG).show()

    companion object {
        const val SELECTION_TRACKER_ID = "video-files-id"
        const val PLAYLIST_NAME_CODE = 2
        const val PLAYLIST_NAME_DIALOG_TAG = "Create Playlist Tag"
    }
}