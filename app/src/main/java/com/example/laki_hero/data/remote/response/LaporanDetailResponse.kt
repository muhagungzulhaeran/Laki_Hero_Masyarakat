package com.example.laki_hero.data.remote.response

import com.google.gson.annotations.SerializedName

data class LaporanDetailResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("gambar")
    val gambar: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("tempat")
    val tempat: String,

    @field:SerializedName("deskripsi")
    val deskripsi: String,

    @field:SerializedName("latitude")
    val latitude: Double? = null,

    @field:SerializedName("longitude")
    val longitude: Double? = null,

    @field:SerializedName("email")
    val email: String,
)
