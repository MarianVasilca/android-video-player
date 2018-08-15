package marian.vasilca.videoplayer.data.repository

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.doctoror.rxcursorloader.RxCursorLoader
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import marian.vasilca.videoplayer.data.model.VideoFile
import marian.vasilca.videoplayer.utilities.schedulers.MainScheduler
import marian.vasilca.videoplayer.utilities.schedulers.Scheduler

/**
 * Repository that handles [VideoFile] objects.
 */
class FilesRepository constructor(
        private val contentResolver: ContentResolver,
        private val mainScheduler: MainScheduler,
        private val ioScheduler: Scheduler
) {

    private val query: RxCursorLoader.Query = RxCursorLoader.Query.Builder()
            .setContentUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setProjection(videoFileProjection)
            .create()

    fun startListeningForVideoFiles(): Flowable<List<VideoFile>> = RxCursorLoader.flowable(contentResolver,
            query, ioScheduler.asRxScheduler(), BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())
            .observeOn(mainScheduler.asRxScheduler())
            .map { consumeCursor(it) }

    private fun consumeCursor(cursor: Cursor): ArrayList<VideoFile> {
        val videoFiles = ArrayList<VideoFile>()

        if (cursor.moveToFirst()) {
            val titleColumn = cursor.getColumnIndex(videoFileProjection[0])
            val displayNameColumn = cursor.getColumnIndex(videoFileProjection[1])
            val idColumn = cursor.getColumnIndex(videoFileProjection[2])
            val dateAddedColumn = cursor.getColumnIndex(videoFileProjection[3])
            val dataColumn = cursor.getColumnIndex(videoFileProjection[4])
            do {
                val videoFile = VideoFile(
                        id = cursor.getLong(idColumn),
                        title = cursor.getString(titleColumn),
                        displayName = cursor.getString(displayNameColumn),
                        dateAdded = cursor.getLong(dateAddedColumn),
                        path = cursor.getString(dataColumn)
                )
                videoFiles.add(videoFile)
            } while (cursor.moveToNext())
            cursor.close()
        }
        cursor.close()

        return videoFiles
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: FilesRepository? = null

        fun getInstance(contentResolver: ContentResolver, mainScheduler: MainScheduler,
                        ioScheduler: Scheduler) =
                instance ?: synchronized(this) {
                    instance ?: FilesRepository(
                            contentResolver = contentResolver,
                            mainScheduler = mainScheduler,
                            ioScheduler = ioScheduler
                    ).also { instance = it }
                }

        var videoFileProjection = arrayOf(
                android.provider.MediaStore.Video.Media.TITLE,
                android.provider.MediaStore.Video.Media.DISPLAY_NAME,
                android.provider.MediaStore.Video.Media._ID,
                android.provider.MediaStore.Video.Media.DATE_ADDED,
                android.provider.MediaStore.Video.Media.DATA
        )
    }
}