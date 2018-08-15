package marian.vasilca.videoplayer.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import marian.vasilca.videoplayer.data.model.Playlist

/**
 * Interface for database access for [Playlist] related operations.
 */
@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(arrayOfPlaylist: Array<Playlist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Playlist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playlist: Playlist)

    @Query("SELECT * FROM playlist_list")
    fun listenForPlaylistItems(): Flowable<List<Playlist>>

    @Query("SELECT * FROM playlist_list WHERE playlistId = :id")
    fun findPlaylistByID(id: Long): Flowable<Playlist>

    @Query("DELETE FROM playlist_list")
    fun deletePlaylistTable()

    @Query("DELETE FROM playlist_list WHERE playlistId = :id")
    fun deletePlaylistByID(id: Long)
}