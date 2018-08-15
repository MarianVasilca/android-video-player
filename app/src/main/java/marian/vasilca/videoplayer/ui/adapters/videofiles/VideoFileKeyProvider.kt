package marian.vasilca.videoplayer.ui.adapters.videofiles

import androidx.recyclerview.selection.ItemKeyProvider
import marian.vasilca.videoplayer.data.model.VideoFile

/**
 * There are three key types: Parcelable, String, and Long. The key type is used to identify
 * selected items.
 * [VideoFile] is Parcelable and can be used as key.
 */
class VideoFileKeyProvider(
        scope: Int,
        private val itemList: List<VideoFile>
) : ItemKeyProvider<VideoFile>(scope) {
    override fun getPosition(videoFile: VideoFile): Int = itemList.indexOf(videoFile)
    override fun getKey(position: Int): VideoFile? = itemList[position]
}