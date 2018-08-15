package marian.vasilca.videoplayer.viewmodels.videoplayer

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * Factory for creating a [VideoPlayerViewModel].
 */
class VideoPlayerViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = VideoPlayerViewModel() as T
}
