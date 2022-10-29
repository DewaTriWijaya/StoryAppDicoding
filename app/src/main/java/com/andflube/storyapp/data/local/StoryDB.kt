package com.andflube.storyapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andflube.storyapp.model.Story

@Entity(tableName = "story_information")
data class StoryDB(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "photoUrl")
    val photoUrl: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: String
) {
    constructor(story: Story) : this(
        id = story.id,
        createdAt = story.createdAt,
        name = story.name,
        photoUrl = story.photoUrl,
        description = story.description,
    )
}