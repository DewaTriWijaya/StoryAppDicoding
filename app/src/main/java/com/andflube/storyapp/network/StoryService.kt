package com.andflube.storyapp.network

import com.andflube.storyapp.network.response.LoginPostResponse
import com.andflube.storyapp.network.response.LoginResponse
import com.andflube.storyapp.network.response.RegisterResponse
import com.andflube.storyapp.network.response.story.AddStoryResponse
import com.andflube.storyapp.network.response.story.GetStoryResponse
import com.andflube.storyapp.ui.mapActivity.ParamLocation
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface StoryService {

    @POST("register")
    suspend fun registerUser(
        @Body regisResponse: RegisterResponse
    ): Response<LoginPostResponse>

    @POST("login")
    suspend fun loginUser(
        @Body loginResponse: LoginResponse
    ): LoginPostResponse

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetStoryResponse

    @GET("stories")
    suspend fun getStoryLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = ParamLocation.LOCATION_FALSE
    ) : GetStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddStoryResponse

}