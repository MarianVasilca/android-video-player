package marian.vasilca.videoplayer.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoFile(
        val id: Long,
        val title: String,
        val displayName: String,
        val dateAdded: Long,
        val path: String
) : Parcelable