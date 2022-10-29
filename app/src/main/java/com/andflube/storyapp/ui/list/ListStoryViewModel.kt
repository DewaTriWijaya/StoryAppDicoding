package com.andflube.storyapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andflube.storyapp.data.DataSource
import com.andflube.storyapp.data.local.StoryDB
import com.andflube.storyapp.model.Story
import kotlinx.coroutines.flow.Flow

class ListStoryViewModel(data: DataSource): ViewModel() {

    //fun getAllStory(token: String) = data.getAllStory(token)

    val story: LiveData<PagingData<StoryDB>> =
        data.getPagedStories()
            .cachedIn(viewModelScope)

    //fun getAlStory() = data.getPagedStories()
    //    .cachedIn(viewModelScope)

}