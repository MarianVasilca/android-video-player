package marian.vasilca.videoplayer.viewmodels.files

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import marian.vasilca.videoplayer.data.repository.FilesRepository

/**
 * Factory for creating a [FilesViewModel] with a constructor that takes a [FilesRepository].
 */
class FilesViewModelFactory(
        private val repository: FilesRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = FilesViewModel(repository) as T
}
