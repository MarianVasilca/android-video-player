package marian.vasilca.videoplayer.data.repository

import io.reactivex.Flowable
import marian.vasilca.videoplayer.data.model.Playlist
import marian.vasilca.videoplayer.db.dao.PlaylistDao
import marian.vasilca.videoplayer.utilities.schedulers.MainScheduler
import marian.vasilca.videoplayer.utilities.schedulers.Scheduler

/**
 * Repository that handles [Playlist] objects.
 */
class PlaylistRepository constructor(
        private val playlistDao: PlaylistDao,
        private val mainScheduler: MainScheduler,
        private val ioScheduler: Scheduler
) {

    fun createPlaylist(playlist: Playlist) {
        // db operation must be executed on a background thread
        ioScheduler.runOnThread {
            playlistDao.insert(playlist)
        }
    }

    fun deletePlaylist(playlistId: Long) =
            ioScheduler.runOnThread {
                playlistDao.deletePlaylistByID(playlistId)
            }

    fun startListeningForPlaylist(): Flowable<List<Playlist>> =
            playlistDao.listenForPlaylistItems()
                    .distinctUntilChanged()
                    .observeOn(mainScheduler.asRxScheduler())
                    .replay(1)
                    .autoConnect(0)

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: PlaylistRepository? = null

        fun getInstance(playlistDao: PlaylistDao, mainScheduler: MainScheduler,
                        ioScheduler: Scheduler) =
                instance ?: synchronized(this) {
                    instance ?: PlaylistRepository(
                            playlistDao = playlistDao,
                            mainScheduler = mainScheduler,
                            ioScheduler = ioScheduler
                    ).also { instance = it }
                }
    }
}