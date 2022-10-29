package com.andflube.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.andflube.storyapp.data.local.StoryDB
import com.andflube.storyapp.model.UserPreference
import com.andflube.storyapp.network.ResultResponse
import com.andflube.storyapp.network.StoryService
import com.andflube.storyapp.network.response.LoginPostResponse
import com.andflube.storyapp.network.response.LoginResponse
import com.andflube.storyapp.network.response.RegisterResponse
import com.andflube.storyapp.network.response.story.AddStoryResponse
import com.andflube.storyapp.paging.StoryRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class)
class DataSource private constructor(
    private val storyService: StoryService,
    //private val storyDao: StoryDao,
    private val storyDatabase: StoryDatabase,
    private val pref: UserPreference
    //private val storiesRemoteMediator: StoryRemoteMediator
) {

    suspend fun registerUser(registerResponse: RegisterResponse): Flow<ResultResponse<Response<LoginPostResponse>>> {
        return flow {
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
                emit(ResultResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun loginUser(loginResponse: LoginResponse): Flow<ResultResponse<LoginPostResponse>> {
        return flow {
            try {
                emit(ResultResponse.Loading)
                val response = storyService.loginUser(loginResponse)
                if (!response.error) {
                    emit(ResultResponse.Success(response))
                } else {
                    emit(ResultResponse.Error(response.message))
                }
            } catch (e: Exception) {
                emit(ResultResponse.Error(e.message.toString()))
            }
        }
    }

    suspend fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<ResultResponse<AddStoryResponse>> {
        return flow {
            try {
                emit(ResultResponse.Loading)
                val response = storyService.addNewStory(token, file, description)
                if (!response.error) {
                    emit(ResultResponse.Success(response))
                } else {
                    emit(ResultResponse.Error(response.message))
                }
            } catch (e: Exception) {
                emit(ResultResponse.Error(e.message.toString()))
            }
        }
    }

/*
    fun getAllStory(token: String): LiveData<ResultResponse<List<StoryDB>>> = liveData {
        emit(ResultResponse.Loading)
        try {
            val response = storyService.getAllStory(token)
            val articles = response.listStory
            val newsList = articles?.map { article ->
                //val isBookmarked = storyDao.isNewsBookmarked(article.title)
                StoryDB(
                    article.id,
                    article.name,
                    article.description,
                    article.photoUrl,
                    article.createdAt
                )
            }
            storyDao.deleteAll()
            newsList?.let { storyDao.insertStory(it) }
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(ResultResponse.Error(e.message.toString()))
        }
        val localData: LiveData<ResultResponse<List<StoryDB>>> = storyDao.getAllStory().map { ResultResponse.Success(it) }
        emitSource(localData)
    }
*/

//    fun getPagedStories(): Flow<PagingData<Story>> {
//        return Pager(
//            config = PagingConfig(pageSize = 5),
//            remoteMediator = storiesRemoteMediator,
//            pagingSourceFactory = { storyDao.getAllStory() }
//        ).flow.map { pagingData ->
//            pagingData .map { it.toStory() }
//        }
//    }

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


    companion object {
        @Volatile
        private var instance: DataSource? = null
        fun getInstance(
            apiService: StoryService,
            storyDatabase: StoryDatabase,
            pref: UserPreference
            //storyDao: StoryDao,
            //remoteMediator: StoryRemoteMediator
        ): DataSource =
            instance ?: synchronized(this) {
                instance ?: DataSource(apiService, storyDatabase, pref)
            }.also { instance = it }
    }

}