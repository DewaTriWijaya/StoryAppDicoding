package com.andflube.storyapp.ui.mapActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andflube.storyapp.data.DataSource
import com.andflube.storyapp.network.ResultResponse
import com.andflube.storyapp.network.response.story.GetStoryResponse


class MapsViewModel(private val data : DataSource) : ViewModel() {

     fun getLocation(token: String): LiveData<ResultResponse<GetStoryResponse>> = data.getLocation(token)

}