package com.example.laki_hero.viewmodel

import androidx.lifecycle.ViewModel
import com.example.laki_hero.data.remote.UserRepository

class RewardViewModel(private val userRepository: UserRepository) : ViewModel() {
    // TODO: Implement the ViewModel
    fun getPoinSaya(email: String) = userRepository.getPoinSaya(email)

    fun getPoinDetail(email: String) = userRepository.getPoinDetail(email)
}