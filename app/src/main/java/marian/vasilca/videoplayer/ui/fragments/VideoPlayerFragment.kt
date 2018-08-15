package marian.vasilca.videoplayer.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Point
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_video_player.*
import marian.vasilca.videoplayer.R
import marian.vasilca.videoplayer.data.model.VideoFile
import marian.vasilca.videoplayer.databinding.FragmentVideoPlayerBinding
import marian.vasilca.videoplayer.di.InjectorUtils
import marian.vasilca.videoplayer.ui.BaseFragment
import marian.vasilca.videoplayer.viewmodels.videoplayer.VideoPlayerViewModel


/**
 * A fragment that shows the video mediaPlayer.
 */
class VideoPlayerFragment : BaseFragment<FragmentVideoPlayerBinding>() {
    override val layoutResource: Int
        get() = R.layout.fragment_video_player

    override val tag: String
        get() = "VideoPlayerFragment"

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var surfaceHolder: SurfaceHolder

    private lateinit var videoPlayerVideoModel: VideoPlayerViewModel

    override fun onBoundViews(savedInstanceState: Bundle?) {
        val factory = InjectorUtils.provideVideoPlayerViewModelFactory()
        videoPlayerVideoModel = ViewModelProviders.of(this, factory).get(VideoPlayerViewModel::class.java)

        // get the playlist from the bundle arguments
        val playlist = arguments?.getParcelableArrayList<VideoFile>(PLAYLIST_TAG) ?: ArrayList()
        val itemPosition = arguments?.getInt(CURRENT_ITEM_TAG) ?: 0
        videoPlayerVideoModel.setPlaylist(playlist)

        surfaceHolder = svVideo.holder
        surfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(p0: SurfaceHolder?) {
                subscribeUI()
                videoPlayerVideoModel.initPlaylistPosition(itemPosition)
            }

            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) = Unit
            override fun surfaceDestroyed(p0: SurfaceHolder?) = Unit
        })

        ibSkipNext.setOnClickListener { videoPlayerVideoModel.updateNextPlaylistPosition() }
        ibPlayPause.setOnClickListener { playPause() }
        ibSkipPrevious.setOnClickListener { videoPlayerVideoModel.updatePreviousPlaylistPosition() }

        videoPlayerVideoModel.initVolume(sbVolume.progress)
        sbVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                videoPlayerVideoModel.setCurrentVolume(progress)
            }
        })
    }

    private fun subscribeUI() {
        videoPlayerVideoModel.uri.observe(this, Observer {
            play(it)
        })
        videoPlayerVideoModel.currentVolume.observe(this, Observer {
            val volume = (it ?: 1.0f)
            mediaPlayer?.setVolume(volume, volume)
        })
    }

    private fun play(uri: Uri?) {
        if (uri == null) {
            Log.e(tag, "uri == null")
            return
        }
        releasePlayer()

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(requireContext(), uri)
        mediaPlayer?.setSurface(surfaceHolder.surface)
        mediaPlayer?.setOnCompletionListener {
            videoPlayerVideoModel.updateNextPlaylistPosition()
        }
        mediaPlayer?.setOnPreparedListener {
            it.start()
        }

        mediaPlayer?.setOnVideoSizeChangedListener { mediaPlayer, videoWidth, videoHeight ->
            setFitToFillAspectRatio(mediaPlayer, videoWidth, videoHeight)
        }

        val volume = (videoPlayerVideoModel.currentVolume.value)?.toFloat() ?: 1.0f
        mediaPlayer?.setVolume(volume, volume)

        mediaPlayer?.prepareAsync()
    }

    private fun setFitToFillAspectRatio(mediaPlayer: MediaPlayer?, videoWidth: Int, videoHeight: Int) {
        if (mediaPlayer != null) {
            val display = requireActivity().windowManager.defaultDisplay
            // display size in pixels
            val size = Point()
            display.getSize(size)
            val screenWidth = size.x
            val screenHeight = size.y
            val videoParams = svVideo.layoutParams

            if (videoWidth > videoHeight) {
                videoParams.width = screenWidth
                videoParams.height = screenWidth * videoHeight / videoWidth
            } else {
                videoParams.width = screenHeight * videoWidth / videoHeight
                videoParams.height = screenHeight
            }

            svVideo.layoutParams = videoParams
        }
    }

    private fun playPause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        // Before API Level 24 there is no guarantee of onStop being called. So we have
        // to release the mediaPlayer as early as possible in onPause.
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        // Starting with API Level 24 (which brought multi and split window mode) onStop
        // is guaranteed to be called and in the paused mode our activity is eventually still visible.
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        // store the playback state to be able to resume properly when our app for instance comes
        // to foreground again
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    companion object {
        const val PLAYLIST_TAG = "playlist"
        const val CURRENT_ITEM_TAG = "item-position"
    }

}
