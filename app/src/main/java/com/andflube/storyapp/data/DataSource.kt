package com.andflube.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.andflube.storyapp.data.local.StoryDB
import com.andflube.storyapp.model.UserPreference
import com.andflube.storyapp.network.ResultResponse
import com.andflube.storyapp.network.StoryService
import com.andflube.storyapp.network.response.LoginPostResponse
import com.andflube.storyapp.network.response.LoginResponse
import com.andflube.storyapp.network.response.RegisterResponse
import com.andflube.storyapp.network.response.story.AddStoryResponse
import com.andflube.storyapp.network.response.story.GetStoryResponse
import com.andflube.storyapp.paging.StoryRemoteMediator
import com.andflube.storyapp.ui.mapActivity.ParamLocation
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class)
class DataSource private constructor(
    private val storyService: StoryService,
    private val storyDatabase: StoryDatabase,
    private val pref: UserPreference
) {
    //private var token: String? = null
    //    suspend fun registerUser(registerResponse: RegisterResponse): Flow<ResultResponse<Response<LoginPostResponse>>> {
//        return flow {
//            try {
//                emit(ResultResponse.Loading)
//                val response = storyService.registerUser(registerResponse)
//                if (response.code() == 201) {
//                    emit(ResultResponse.Success(response))
//                } else if (response.code() == 400) {
//                    val errorBody = JSONObject(response.errorBody().toString())
//                    emit(ResultResponse.Error(errorBody.getString("message")))
//                }
//            } catch (e: Exception) {
//                emit(ResultResponse.Error(e.message.toString()))
//            }
//        }
//    }
    fun registerUser(registerResponse: RegisterResponse): LiveData<ResultResponse<Response<LoginPostResponse>>> = liveData {
        try {
            emit(ResultResponse.Loading)
                val response = storyService.registerUser(registerResponse)
                if (response.code() == 201) {
                    emit(ResultResponse.Success(response))
                } else if (response.code() == 400) {
                    val errorBody = JSONObject(response.errorBody().toString())
                    emit(ResultResponse.Error(errorBody.getString("message")))
                }
        } catch (e: Exception) {
            Log.d("RegisterUser", "getRegisterUser: ${e.message.toString()} ")
            emit(ResultResponse.Error(e.message.toString()))
        }
    }


//    suspend fun loginUser(loginResponse: LoginResponse): Flow<ResultResponse<LoginPostResponse>> {
//        return flow {
//            try {
//                emit(ResultResponse.Loading)
//                val response = storyService.loginUser(loginResponse)
//                if (!response.error) {
//                    emit(ResultResponse.Success(response))
//                } else {
//                    emit(ResultResponse.Error(response.message))
//                }
//            } catch (e: Exception) {
//                emit(ResultResponse.Error(e.message.toString()))
//            }
//        }
//    }

    fun loginUser(loginResponse: LoginResponse): LiveData<ResultResponse<LoginPostResponse>> = liveData {
        try {
            emit(ResultResponse.Loading)
            val response = storyService.loginUser(loginResponse)
            if (!response.error) {
                emit(ResultResponse.Success(response))
            } else {
                emit(ResultResponse.Error(response.message))
            }
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

//    suspend fun addNewStory(
//        token: String,
//        file: MultipartBody.Part,
//        description: RequestBody
//    ): Flow<ResultResponse<AddStoryResponse>> {
//        return flow {
//            try {
//                emit(ResultResponse.Loading)
//                val response = storyService.addNewStory(token, file, description)
//                if (!response.error) {
//                    emit(ResultResponse.Success(response))
//                } else {
//                    emit(ResultResponse.Error(response.message))
//                }
//            } catch (e: Exception) {
//                emit(ResultResponse.Error(e.message.toString()))
//            }
//        }
//    }

    fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<ResultResponse<AddStoryResponse>> = liveData {
        try {
            val response = storyService.addNewStory(token, file, description)
                if (!response.error) {
                    emit(ResultResponse.Success(response))
                } else {
                    emit(ResultResponse.Error(response.message))
                }
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun getPagedStories(): LiveData<PagingData<StoryDB>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, storyService, pref),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

//    fun getLocation(): LiveData<ResultResponse<List<Story>>> = liveData {
//
//        emit(ResultResponse.Loading)
//        try {
//            token = pref.token
//            val bearerToken = "Bearer $token"
//
//            val response = storyService.getStoryLocation(
//                token = bearerToken,
//                location = ParamLocation.LOCATION_TRUE
//            )
//
//            when (response.error) {
//                true -> emit(ResultResponse.Error(
//                    response.message.let { "Error" }
//                ))
//                false -> emit(
//                    response.listStory?.let {
//                        if (it.isNotEmpty()) ResultResponse.Success(it) else null
//                    } ?: ResultResponse.Error("Error")
//                )
//            }
//        } catch (t: Throwable) {
//            emit(ResultResponse.Error(t.message.toString()))
//        }
//    }

    fun getLocation(token: String): LiveData<ResultResponse<GetStoryResponse>> = liveData {
        emit(ResultResponse.Loading)
        try {
            //val bearerToken = "Bearer $token"

            val response = storyService.getStoryLocation(
                token = token,
                location = ParamLocation.LOCATION_TRUE
            )

            emit(ResultResponse.Loading)
            if (!response.error) {
                emit(ResultResponse.Success(response))
            } else {
                emit(ResultResponse.Error(response.message))
            }
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(ResultResponse.Error(e.message.toString()))
        }
    }



    companion object {
        @Volatile
        private var instance: DataSource? = null
        fun getInstance(
            apiService: StoryService,
            storyDatabase: StoryDatabase,
            pref: UserPreference
        ): DataSource =
            instance ?: synchronized(this) {
                instance ?: DataSource(apiService, storyDatabase, pref)
            }.also { instance = it }
    }

}