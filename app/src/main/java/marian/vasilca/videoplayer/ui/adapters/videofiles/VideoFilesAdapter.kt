package marian.vasilca.videoplayer.ui.adapters.videofiles

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import marian.vasilca.videoplayer.R
import marian.vasilca.videoplayer.data.model.VideoFile
import marian.vasilca.videoplayer.databinding.VideoFileItemBinding
import marian.vasilca.videoplayer.ui.adapters.base.DataBoundListAdapter


class VideoFilesAdapter(
        private val callback: ((VideoFile, Int) -> Unit)?
) : DataBoundListAdapter<VideoFile, VideoFileItemBinding>(
        diffCallback = object : DiffUtil.ItemCallback<VideoFile>() {
            override fun areItemsTheSame(oldItem: VideoFile, newItem: VideoFile): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: VideoFile, newItem: VideoFile): Boolean {
                return oldItem.title == newItem.title
            }
        }
) {

    private var selectionTracker: SelectionTracker<VideoFile>? = null

    fun setSelectionTracker(selectionTracker: SelectionTracker<VideoFile>?) {
        this.selectionTracker = selectionTracker
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): VideoFileItemBinding {
        val binding = DataBindingUtil
                .inflate<VideoFileItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.video_file_item,
                        parent,
                        false
                )
        binding.root.setOnClickListener { _ ->
            binding.videoFile?.let {
                callback?.invoke(it, binding.position!!)
            }
        }
        return binding
    }

    override fun bind(binding: VideoFileItemBinding, item: VideoFile, position: Int) {
        binding.videoFile = item
        binding.position = position
        binding.videoItemDetail = VideoFileDetail(position, item)
        binding.clVideoFile.isActivated = selectionTracker?.isSelected(item) ?: false
    }

}