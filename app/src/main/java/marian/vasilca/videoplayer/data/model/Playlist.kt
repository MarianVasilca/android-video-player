package marian.vasilca.videoplayer.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "playlist_list")
data class Playlist(
        val title: String,
        val files: List<VideoFile>,
        val dateAdded: Long
) {
    @PrimaryKey(autoGenerate = true)
    var playlistId: Long? = null


}