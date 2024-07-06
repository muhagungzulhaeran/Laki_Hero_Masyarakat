package com.example.laki_hero.viewmodel

import androidx.lifecycle.ViewModel
import com.example.laki_hero.data.remote.UserRepository
import java.io.File

class TambahLaporanViewModel(private val repository: UserRepository): ViewModel() {
    fun uploadImage(file: File, email: String, tempat: String, deskripsi: String, latitude: String, longitude: String) = repository.uploadImage(file, email, tempat, deskripsi, latitude, longitude)
}