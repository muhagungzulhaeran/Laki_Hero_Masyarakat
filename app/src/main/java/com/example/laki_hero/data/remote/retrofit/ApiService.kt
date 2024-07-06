package com.example.laki_hero.data.remote.retrofit

import com.example.laki_hero.data.remote.response.LaporanResponse
import com.example.laki_hero.data.remote.response.LoginResponse
import com.example.laki_hero.data.remote.response.PoinDetailResponse
import com.example.laki_hero.data.remote.response.PoinResponse
import com.example.laki_hero.data.remote.response.RegisterResponse
import com.example.laki_hero.data.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("nama")nama: String,
        @Field("email")email: String,
        @Field("password")password: String,
        @Field("token_fcm")token_fcm: String
    ):RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password")password: String
    ):LoginResponse

    @GET("semua_laporan")
    suspend fun getlaporanSaya(@Query("email") email: String
    ): LaporanResponse

//    @GET("riwayat_laporan")
//    suspend fun getRiwayatLaporanSaya(
//    ): LaporanResponse

    @GET("riwayat_laporan")
    suspend fun getRiwayatLaporanSaya(@Query("email") email: String
    ): LaporanResponse

    @GET("getUserDetails.php")
    fun getSpesifikLaporan(@Query("email") email: String
    ): LaporanResponse

    @Multipart
//    @POST("tambah_laporan")
    @POST("api_jarak.php")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part ("email")email: RequestBody,
        @Part("tempat") tempat: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): UploadResponse

    @GET("api_list_poin.php")
    suspend fun getPoinSaya(@Query("email") email: String
    ): PoinResponse

    @GET("api_poin_detail.php")
    suspend fun poinDetail(
        @Query("email") email: String
    ): PoinDetailResponse
}