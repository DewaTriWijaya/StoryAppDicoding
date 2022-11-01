package com.andflube.storyapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andflube.storyapp.data.DataSource
import com.andflube.storyapp.data.local.StoryDB

class ListStoryViewModel(data: DataSource): ViewModel() {

    val story: LiveData<PagingData<StoryDB>> =
        data.getPagedStories()
            .cachedIn(viewModelScope)
}