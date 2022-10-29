package com.andflube.storyapp.ui.welcome.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andflube.storyapp.data.DataSource
import com.andflube.storyapp.network.ResultResponse
import com.andflube.storyapp.network.response.LoginPostResponse
import com.andflube.storyapp.network.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class SignupViewModel(private val data : DataSource) : ViewModel() {

    fun registerUser(registerResponse: RegisterResponse): LiveData<ResultResponse<Response<LoginPostResponse>>>{
        val result = MutableLiveData<ResultResponse<Response<LoginPostResponse>>>()
        viewModelScope.launch {
            data.registerUser(registerResponse).collect{
                result.postValue(it)
            }
        }
        return result
    }
}