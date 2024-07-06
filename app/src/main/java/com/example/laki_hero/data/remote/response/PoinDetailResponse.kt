package com.example.laki_hero.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PoinDetailResponse(

    @field:SerializedName("poin")
    val listPoinDetailSaya: List<ListPoinDetailSayaItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

)

@Parcelize
data class ListPoinDetailSayaItem(

    @field:SerializedName("poin")
    val poin: String

): Parcelable
