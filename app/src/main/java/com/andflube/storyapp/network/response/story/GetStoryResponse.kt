package com.andflube.storyapp.network.response.story

import com.andflube.storyapp.data.local.StoryDB
import com.andflube.storyapp.model.Story
import com.google.gson.annotations.SerializedName

data class GetStoryResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("listStory")
    val listStory: List<Story>
)