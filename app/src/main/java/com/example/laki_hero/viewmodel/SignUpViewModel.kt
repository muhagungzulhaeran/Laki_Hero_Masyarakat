package com.example.laki_hero.viewmodel

import androidx.lifecycle.ViewModel
import com.example.laki_hero.data.remote.UserRepository

class SignUpViewModel (private val registerRepository: UserRepository): ViewModel() {
    fun postSignup(nama : String, email: String, password: String, token_fcm: String) =
        registerRepository.postRegister(nama, email, password, token_fcm)
}