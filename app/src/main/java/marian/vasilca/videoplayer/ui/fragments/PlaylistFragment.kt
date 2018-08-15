package marian.vasilca.videoplayer.ui.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import androidx.navigation.findNavController
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_playlists.*
import marian.vasilca.videoplayer.R
import marian.vasilca.videoplayer.data.model.VideoFile
import marian.vasilca.videoplayer.databinding.FragmentLocalFilesBinding
import marian.vasilca.videoplayer.databinding.PlaylistItemBinding
import marian.vasilca.videoplayer.di.InjectorUtils
import marian.vasilca.videoplayer.ui.BaseFragment
import marian.vasilca.videoplayer.ui.adapters.base.DataBoundViewHolder
import marian.vasilca.videoplayer.ui.adapters.playlist.PlaylistAdapter
import marian.vasilca.videoplayer.ui.common.RecyclerItemTouchHelper
import marian.vasilca.videoplayer.utilities.autoCleared
import marian.vasilca.videoplayer.viewmodels.playlist.PlaylistViewModel


/**
 * A fragment that shows the playlist that the user has created.
 */
class PlaylistFragment : BaseFragment<FragmentLocalFilesBinding>(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    override val layoutResource: Int
        get() = R.layout.fragment_playlists

    override val tag: String
        get() = "PlaylistFragment"

    private var adapter by autoCleared<PlaylistAdapter>()

    private lateinit var playlistViewModel: PlaylistViewModel
    override fun onBoundViews(savedInstanceState: Bundle?) {
        val factory = InjectorUtils.providePlaylistViewModelFactory(
                requireContext().applicationContext)
        playlistViewModel = ViewModelProviders.of(this, factory).get(PlaylistViewModel::class.java)

        fabCreate.setOnClickListener { it.findNavController().navigate(R.id.create_playlist_action) }

        setAdapter()
        subscribeUI()
    }

    private fun setAdapter() {
        adapter = PlaylistAdapter {
            val bundle = Bundle()
            bundle.putParcelableArrayList(VideoPlayerFragment.PLAYLIST_TAG, ArrayList<VideoFile>(it.files))
            view?.findNavController()?.navigate(R.id.video_player_action, bundle)
        }

        rvPlaylist.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(
                    this@PlaylistFragment.requireContext(),
                    LinearLayoutManager.VERTICAL)
            )

            adapter = this@PlaylistFragment.adapter
        }

        // adding item touch helper
        // only ItemTouchHelper.RIGHT added to detect Left to Right swipe
        val itemTouchHelperCallback = RecyclerItemTouchHelper(0,
                ItemTouchHelper.RIGHT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvPlaylist)
    }

    private fun subscribeUI() {
        compositeDisposable += playlistViewModel.observePlaylistItems(onNext = { list ->
            adapter.submitList(list)
        })
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is DataBoundViewHolder<*>) {
            val playlistViewHolder = (viewHolder as DataBoundViewHolder<PlaylistItemBinding>)
            val playlist = playlistViewHolder.binding.playlist
            if (playlist?.playlistId != null) {
                playlistViewModel.deletePlaylist(playlistId = playlist.playlistId!!)
            }
        }

    }
}
