package marian.vasilca.videoplayer.ui.common

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import marian.vasilca.videoplayer.databinding.PlaylistItemBinding
import marian.vasilca.videoplayer.ui.adapters.base.DataBoundViewHolder

@Suppress("UNCHECKED_CAST")
/**
 * Helper class for receiving swipe events on ViewHolders
 */
class RecyclerItemTouchHelper(
        dragDirs: Int,
        swipeDirs: Int,
        private val listener: RecyclerItemTouchHelperListener
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            if (viewHolder is DataBoundViewHolder<*>) {
                val foregroundViewHolder = (viewHolder as DataBoundViewHolder<PlaylistItemBinding>)
                val foregroundView = foregroundViewHolder.binding.clForeground
                getDefaultUIUtil().onSelected(foregroundView)
            }
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                                 viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                 actionState: Int, isCurrentlyActive: Boolean) {
        if (viewHolder is DataBoundViewHolder<*>) {
            val foregroundViewHolder = viewHolder as DataBoundViewHolder<PlaylistItemBinding>
            val foregroundView = foregroundViewHolder.binding.clForeground
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is DataBoundViewHolder<*>) {
            val foregroundViewHolder = viewHolder as DataBoundViewHolder<PlaylistItemBinding>
            val foregroundView = foregroundViewHolder.binding.clForeground
            getDefaultUIUtil().clearView(foregroundView)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        if (viewHolder is DataBoundViewHolder<*>) {
            val foregroundViewHolder = viewHolder as DataBoundViewHolder<PlaylistItemBinding>
            val foregroundView = foregroundViewHolder.binding.clForeground
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is DataBoundViewHolder<*>) {
            listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
        }
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}
 
 