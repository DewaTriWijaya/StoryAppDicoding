package com.andflube.storyapp.di

import android.content.Context
import androidx.paging.RemoteMediator
import com.andflube.storyapp.data.DataSource
import com.andflube.storyapp.data.StoryDatabase
import com.andflube.storyapp.model.UserPreference
import com.andflube.storyapp.network.ApiRetrofit
import com.andflube.storyapp.paging.StoryRemoteMediator

object Injection {
    fun provideRepository(context: Context): DataSource {
        val apiService = ApiRetrofit.getStoryService()
        val database = StoryDatabase.getInstance(context)
        val pref = UserPreference(context)
        return DataSource.getInstance(apiService, database, pref)
    }
}