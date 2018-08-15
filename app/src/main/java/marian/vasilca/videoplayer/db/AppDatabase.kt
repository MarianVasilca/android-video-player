package marian.vasilca.videoplayer.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import marian.vasilca.videoplayer.data.model.Playlist
import marian.vasilca.videoplayer.db.dao.PlaylistDao
import marian.vasilca.videoplayer.utilities.DATABASE_NAME

/**
 * The Room database for this app
 */
@Database(entities = [Playlist::class], version = 1, exportSchema = false)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    // add dao here
    abstract fun playlistDao(): PlaylistDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create the database.
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .build()
        }
    }
}
