package marian.vasilca.videoplayer.viewmodels.videoplayer

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import marian.vasilca.videoplayer.data.model.VideoFile
import marian.vasilca.videoplayer.utilities.toUri

class VideoPlayerViewModel : ViewModel() {

    // CurrentPlaylistPosition objects
    private val _currentPlaylistPosition = MutableLiveData<Int>()

    private fun setCurrentPlaylistPosition(currentPlaylistPosition: Int) {
        _currentPlaylistPosition.value = currentPlaylistPosition

        val file = _playlist.value?.get(currentPlaylistPosition)
        setUri(file?.path?.toUri())
    }

    fun initPlaylistPosition(position: Int) {
        if (_currentPlaylistPosition.value == null) {
            setCurrentPlaylistPosition(position)
        }
    }

    // Playlist objects
    private val _playlist = MutableLiveData<List<VideoFile>>()

    fun setPlaylist(playlist: List<VideoFile>) {
        if (_playlist.value != playlist) {
            _playlist.value = playlist
        }
    }

    fun updateNextPlaylistPosition() {
        var value = _currentPlaylistPosition.value ?: 0
        value++
        if (value >= (_playlist.value?.size ?: 0)) {
            value = 0
        }
        setCurrentPlaylistPosition(value)
    }

    fun updatePreviousPlaylistPosition() {
        var value = _currentPlaylistPosition.value ?: 0
        value--
        if (value < 0) {
            value = _playlist.value?.lastIndex ?: 0
        }
        setCurrentPlaylistPosition(value)
    }

    // Current uri objects
    private val _uri = MutableLiveData<Uri?>()
    val uri: LiveData<Uri?>
        get() = _uri

    private fun setUri(uri: Uri?) {
        _uri.value = uri
    }

    // Current volume objects
    private val _currentVolume = MutableLiveData<Float>()
    val currentVolume: LiveData<Float>
        get() = _currentVolume

    fun setCurrentVolume(progress: Int) {
        val computeVolume = computeVolume(progress)
        if (_currentVolume.value != computeVolume) {
            _currentVolume.value = computeVolume
        }
    }

    fun initVolume(progress: Int) {
        if (_currentVolume.value == null) {
            setCurrentVolume(progress)
        }
    }

    private fun computeVolume(progress: Int): Float {
        val max = VideoPlayerViewModel.VOLUME_MAX
        return (1 - Math.log(max - progress.toDouble()) / Math.log(max.toDouble())).toFloat()
    }

    companion object {
        const val VOLUME_MAX = 100.0f
    }
}