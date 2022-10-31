package com.andflube.storyapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andflube.storyapp.data.DataSource
import com.andflube.storyapp.network.ResultResponse
import com.andflube.storyapp.network.response.story.AddStoryResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val data: DataSource) : ViewModel() {

//    fun addNewStory(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<ResultResponse<AddStoryResponse>> {
//        val result = MutableLiveData<ResultResponse<AddStoryResponse>>()
//        viewModelScope.launch {
//            data.addNewStory(token, file, description).collect {
//                result.postValue(it)
//            }
//        }
//        return result
//    }

    fun getAddNewStory(token: String, file: MultipartBody.Part, description: RequestBody) = data.addNewStory(token, file, description)

}