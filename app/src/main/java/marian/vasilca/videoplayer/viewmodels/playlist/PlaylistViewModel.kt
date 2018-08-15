package marian.vasilca.videoplayer.viewmodels.playlist

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import marian.vasilca.videoplayer.data.model.Playlist
import marian.vasilca.videoplayer.data.repository.PlaylistRepository

class PlaylistViewModel(
        private val playlistRepository: PlaylistRepository
) : ViewModel() {

    fun createPlaylist(playlist: Playlist) = playlistRepository.createPlaylist(playlist)
    fun deletePlaylist(playlistId: Long) = playlistRepository.deletePlaylist(playlistId)

    private val playlistItems = playlistRepository.startListeningForPlaylist()

    fun observePlaylistItems(onNext: (List<Playlist>) -> Unit, onError: (Throwable) -> Unit = {}): Disposable =
            playlistItems.subscribeBy(onNext = onNext, onError = onError)
}