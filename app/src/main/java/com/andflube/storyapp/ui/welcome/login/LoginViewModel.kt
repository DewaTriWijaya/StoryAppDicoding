package com.andflube.storyapp.ui.welcome.login

import androidx.lifecycle.*
import com.andflube.storyapp.data.DataSource
import com.andflube.storyapp.network.ResultResponse
import com.andflube.storyapp.network.response.LoginPostResponse
import com.andflube.storyapp.network.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val data: DataSource) : ViewModel() {

//    fun loginUser(loginResponse: LoginResponse): LiveData<ResultResponse<LoginPostResponse>>{
//        val result = MutableLiveData<ResultResponse<LoginPostResponse>>()
//        viewModelScope.launch {
//            data.loginUser(loginResponse).collect{
//                result.postValue(it)
//            }
//        }
//        return result
//    }

    fun getLoginUser(loginResponse: LoginResponse) = data.loginUser(loginResponse)
}