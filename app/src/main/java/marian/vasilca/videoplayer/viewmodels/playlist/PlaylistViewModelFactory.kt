package marian.vasilca.videoplayer.viewmodels.playlist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import marian.vasilca.videoplayer.data.repository.PlaylistRepository

/**
 * Factory for creating a [PlaylistViewModel] with a constructor that takes a [PlaylistRepository].
 */
class PlaylistViewModelFactory(
        private val repository: PlaylistRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = PlaylistViewModel(repository) as T
}
