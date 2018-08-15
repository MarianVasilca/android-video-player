package marian.vasilca.videoplayer.ui.adapters.playlist

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import marian.vasilca.videoplayer.R
import marian.vasilca.videoplayer.data.model.Playlist
import marian.vasilca.videoplayer.databinding.PlaylistItemBinding
import marian.vasilca.videoplayer.ui.adapters.base.DataBoundListAdapter

class PlaylistAdapter(
        private val callback: ((Playlist) -> Unit)?
) : DataBoundListAdapter<Playlist, PlaylistItemBinding>(
        diffCallback = object : DiffUtil.ItemCallback<Playlist>() {
            override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
                    oldItem.playlistId == newItem.playlistId

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
                    oldItem.title == newItem.title && oldItem.files.size == newItem.files.size
        }
) {

    override fun createBinding(parent: ViewGroup, viewType: Int): PlaylistItemBinding {
        val binding = DataBindingUtil
                .inflate<PlaylistItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.playlist_item,
                        parent,
                        false
                )
        binding.root.setOnClickListener { _ ->
            binding.playlist?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: PlaylistItemBinding, item: Playlist, position: Int) {
        binding.playlist = item
    }

}