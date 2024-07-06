package com.example.laki_hero.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PoinResponse(

    @field:SerializedName("listlaporan")
    val listPoinSaya: List<ListPoinSayaItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

@Parcelize
data class ListPoinSayaItem(

    @field:SerializedName("id_poin")
    val id_poin: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("poin")
    val poin: String,

    @field:SerializedName("id_laporan")
    val id_laporan: String,

    @field:SerializedName("tanggal")
    val tanggal: String,

): Parcelable
