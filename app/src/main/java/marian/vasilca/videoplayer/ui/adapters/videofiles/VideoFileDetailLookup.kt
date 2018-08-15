package marian.vasilca.videoplayer.ui.adapters.videofiles

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import marian.vasilca.videoplayer.data.model.VideoFile
import marian.vasilca.videoplayer.databinding.VideoFileItemBinding
import marian.vasilca.videoplayer.ui.adapters.base.DataBoundViewHolder

/**
 * The Selection library calls getItemDetails(MotionEvent) when it needs access to information
 * about the area and/or ItemDetailsLookup.ItemDetails under a MotionEvent. This implementation
 * must negotiate ViewHolder lookup with the corresponding RecyclerView instance,
 * and the subsequent conversion of the ViewHolder instance to an ItemDetailsLookup.ItemDetails instance.
 *
 * @see [https://developer.android.com/reference/androidx/recyclerview/selection/ItemDetailsLookup]
 */
internal class VideoFileDetailLookup(
        private val recyclerView: RecyclerView
) : ItemDetailsLookup<VideoFile>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<VideoFile>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val holder = recyclerView.getChildViewHolder(view)
            if (holder is DataBoundViewHolder<*>) {
                return (holder.binding as VideoFileItemBinding).videoItemDetail
            }
        }
        return null
    }
}