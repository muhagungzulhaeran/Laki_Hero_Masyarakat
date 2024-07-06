package com.example.laki_hero.viewmodel

import androidx.lifecycle.ViewModel
import com.example.laki_hero.data.remote.UserRepository

class RiwayatLaporanViewModel(private val repository: UserRepository) : ViewModel() {
    // TODO: Implement the ViewModel
    fun getRiwayatLaporanSaya(email: String) = repository.getRiwayatLaporanSaya(email)

   // fun getSpesifikLaporan(email: String) = repository.getSpesifikLaporan(email)
}
