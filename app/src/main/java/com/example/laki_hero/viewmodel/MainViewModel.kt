package com.example.laki_hero.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.laki_hero.data.remote.UserRepository
import com.example.laki_hero.data.local.datastore.UserModel

class MainViewModel(private val repository: UserRepository): ViewModel() {
//    fun getStories() = repository.getStory()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}