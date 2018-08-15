package marian.vasilca.videoplayer.viewmodels.files

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import marian.vasilca.videoplayer.data.model.VideoFile
import marian.vasilca.videoplayer.data.repository.FilesRepository

class FilesViewModel(
        filesRepository: FilesRepository
) : ViewModel() {

    private val playlistItems = filesRepository.startListeningForVideoFiles()

    fun observeFiles(onNext: (List<VideoFile>) -> Unit, onError: (Throwable) -> Unit = {}): Disposable =
            playlistItems.subscribeBy(onNext = onNext, onError = onError)
}