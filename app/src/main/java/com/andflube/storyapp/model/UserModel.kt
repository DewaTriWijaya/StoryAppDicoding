package com.andflube.storyapp.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("userId")
    val userId: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("token")
    val token: String,

    var email: String,
    var password: String,
    var isLogin: Boolean
)