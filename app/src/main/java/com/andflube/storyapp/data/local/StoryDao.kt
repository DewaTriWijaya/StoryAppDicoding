package com.andflube.storyapp.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface StoryDao {

    //@Query("SELECT * FROM story_information ORDER BY createdAt DESC")
    //fun getAllStory(): LiveData<List<StoryDB>>

    @Query("SELECT * FROM story_information")
    fun getAllStory(): PagingSource<Int, StoryDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(news: List<StoryDB>)

    @Query("DELETE FROM story_information")
    suspend fun deleteAll()
}