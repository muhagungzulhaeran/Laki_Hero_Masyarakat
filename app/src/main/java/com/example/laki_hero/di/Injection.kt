package com.example.laki_hero.di

import android.content.Context
import com.example.laki_hero.data.remote.UserRepository
import com.example.laki_hero.data.local.datastore.UserPreferences
import com.example.laki_hero.data.local.datastore.dataStore
import com.example.laki_hero.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}