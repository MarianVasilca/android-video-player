package marian.vasilca.videoplayer.ui.adapters.videofiles

import androidx.recyclerview.selection.ItemDetailsLookup
import marian.vasilca.videoplayer.data.model.VideoFile

/**
 * ItemDetails implementation provides the selection library with access to information about
 * a specific RecyclerView item. This class is a key component in controlling the behaviors
 * of the selection library in the context of a specific activity.
 */
class VideoFileDetail(
        private val adapterPosition: Int,
        private val selectionKey: VideoFile
) : ItemDetailsLookup.ItemDetails<VideoFile>() {
    override fun getPosition(): Int = adapterPosition
    override fun getSelectionKey() = selectionKey
}