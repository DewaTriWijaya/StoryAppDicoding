package com.andflube.storyapp.network.response

import com.andflube.storyapp.model.UserModel
import com.google.gson.annotations.SerializedName

data class LoginPostResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("loginResult")
    val loginResult: UserModel
)