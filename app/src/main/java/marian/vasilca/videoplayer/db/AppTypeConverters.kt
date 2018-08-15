package marian.vasilca.videoplayer.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import marian.vasilca.videoplayer.data.model.VideoFile
import java.util.*

/**
 * Type converters to allow Room to reference complex data types.
 */
class AppTypeConverters {

    @TypeConverter
    fun stringToVideoFileList(data: String?): List<VideoFile> {
        if (data == null) return Collections.emptyList()
        val listType = object : TypeToken<List<VideoFile>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun videoFileListToString(objects: List<VideoFile>): String = Gson().toJson(objects)
}