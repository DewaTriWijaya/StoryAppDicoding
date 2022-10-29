package com.andflube.storyapp.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
