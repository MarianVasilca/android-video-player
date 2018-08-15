package marian.vasilca.videoplayer.di

import android.content.Context
import marian.vasilca.videoplayer.data.repository.FilesRepository
import marian.vasilca.videoplayer.data.repository.PlaylistRepository
import marian.vasilca.videoplayer.db.AppDatabase
import marian.vasilca.videoplayer.utilities.schedulers.IoScheduler
import marian.vasilca.videoplayer.utilities.schedulers.MainScheduler
import marian.vasilca.videoplayer.viewmodels.files.FilesViewModelFactory
import marian.vasilca.videoplayer.viewmodels.playlist.PlaylistViewModelFactory
import marian.vasilca.videoplayer.viewmodels.videoplayer.VideoPlayerViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {
    private fun getFilesRepository(context: Context): FilesRepository {
        return FilesRepository.getInstance(
                contentResolver = context.contentResolver,
                mainScheduler = MainScheduler(),
                ioScheduler = IoScheduler()
        )
    }

    fun provideFilesViewModelFactory(context: Context): FilesViewModelFactory {
        val repository = getFilesRepository(context)
        return FilesViewModelFactory(repository)
    }

    private fun getPlaylistRepository(context: Context): PlaylistRepository {
        return PlaylistRepository.getInstance(
                AppDatabase.getInstance(context).playlistDao(),
                MainScheduler(),
                IoScheduler()
        )
    }

    fun providePlaylistViewModelFactory(context: Context): PlaylistViewModelFactory {
        val repository = getPlaylistRepository(context)
        return PlaylistViewModelFactory(repository)
    }

    fun provideVideoPlayerViewModelFactory(): VideoPlayerViewModelFactory {
        return VideoPlayerViewModelFactory()
    }
}