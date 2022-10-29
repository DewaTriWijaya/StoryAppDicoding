package com.andflube.storyapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andflube.storyapp.data.local.StoryDB
import com.andflube.storyapp.data.local.StoryDao
import com.andflube.storyapp.paging.RemoteKeys
import com.andflube.storyapp.paging.RemoteKeysDao

@Database(
    entities = [StoryDB::class, RemoteKeys::class],
    version = 2,
    exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @Synchronized
        fun getInstance(context: Context): StoryDatabase {
            val instanceA = INSTANCE
            if (instanceA != null) {
                return instanceA
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java,
                    "story_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}