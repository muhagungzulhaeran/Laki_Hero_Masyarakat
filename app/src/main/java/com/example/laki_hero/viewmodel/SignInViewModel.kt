package com.example.laki_hero.viewmodel

import androidx.lifecycle.ViewModel
import com.example.laki_hero.data.remote.UserRepository
import com.example.laki_hero.data.local.datastore.UserModel

class SignInViewModel(private val repository: UserRepository): ViewModel() {
    fun signin(email: String, password: String) = repository.postLogin(email,password)
    suspend fun saveSession(userModel: UserModel){
        repository.saveSession(userModel)
    }
}