package com.example.laki_hero.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.laki_hero.data.local.datastore.UserModel
import com.example.laki_hero.data.remote.UserRepository

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getLaporanSaya(email: String) = repository.getLaporanSaya(email)
}