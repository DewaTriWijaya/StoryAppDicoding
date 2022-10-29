package com.andflube.storyapp.network

sealed class ResultResponse<out R> {
    object Loading: ResultResponse<Nothing>()
    data class Success<out T>(val data: T) : ResultResponse<T>()
    data class Error(val error: String): ResultResponse<Nothing>()
}


