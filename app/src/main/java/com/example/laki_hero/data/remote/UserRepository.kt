package com.example.laki_hero.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.laki_hero.data.local.datastore.UserModel
import com.example.laki_hero.data.local.datastore.UserPreferences
import com.example.laki_hero.data.remote.response.ErrorResponse
import com.example.laki_hero.data.remote.response.LaporanResponse
import com.example.laki_hero.data.remote.response.ListLaporanSayaItem
import com.example.laki_hero.data.remote.response.ListPoinDetailSayaItem
import com.example.laki_hero.data.remote.response.ListPoinSayaItem
import com.example.laki_hero.data.remote.response.LoginResponse
import com.example.laki_hero.data.remote.response.PoinDetailResponse
import com.example.laki_hero.data.remote.response.RegisterResponse
import com.example.laki_hero.data.remote.response.UploadResponse
import com.example.laki_hero.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class UserRepository (
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
){
    fun postRegister(nama: String, email: String, password: String, token_fcm: String): LiveData<Result<RegisterResponse>> = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.register(nama, email, password,token_fcm)
            emit(Result.Success(response))
        } catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(error, ErrorResponse::class.java)
            emit(Result.Error(errorBody.message))
        }
    }
    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e:HttpException){
            val error = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(error, ErrorResponse::class.java)
            emit(Result.Error(errorBody.message))
        }
    }

//    fun getSpesifikLaporan(email: String): LiveData<Result<List<ListLaporanSayaItem>>>  {
//        val result = MutableLiveData<Result<LaporanResponse>>()
//        result.value = Result.Loading
//
//        apiService.getSpesifikLaporan(email).enqueue(object : Callback<LaporanResponse> {
//            override fun onResponse(call: Call<LaporanResponse>, response: Response<LaporanResponse>) {
//                if (response.isSuccessful) {
//                    result.value = Result.Success(response.body()!!)
//                } else {
//                    result.value = Result.Error(response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<LaporanResponse>, t: Throwable) {
//                result.value = Result.Error(t.message.toString())
//            }
//        })
//
//        return result
//    }


    fun getLaporanSaya(email: String): LiveData<Result<List<ListLaporanSayaItem>>> = liveData{
        emit(Result.Loading)
        try{
            val response = apiService.getlaporanSaya(email)
            emit(Result.Success(response.listLaporanSaya))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }catch (e: Exception){
            emit(Result.Error(e.message ?: "Error"))
        }
    }

    fun getPoinSaya(email: String): LiveData<Result<List<ListPoinSayaItem>>> = liveData{
        emit(Result.Loading)
        try{
            val response = apiService.getPoinSaya(email)
            emit(Result.Success(response.listPoinSaya))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }catch (e: Exception){
            emit(Result.Error(e.message ?: "Error"))
        }
    }

    fun getPoinDetail(email: String): LiveData<Result<List<ListPoinDetailSayaItem>>> = liveData{
        emit(Result.Loading)
        try{
            val response = apiService.poinDetail(email)
            emit(Result.Success(response.listPoinDetailSaya))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }catch (e: Exception){
            emit(Result.Error(e.message ?: "Error"))
        }
    }

    fun getRiwayatLaporanSaya(email: String): LiveData<Result<List<ListLaporanSayaItem>>> = liveData{
        emit(Result.Loading)
        try{
            val response = apiService.getRiwayatLaporanSaya(email)
            emit(Result.Success(response.listLaporanSaya))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }catch (e: Exception){
            emit(Result.Error(e.message ?: "Error"))
        }
    }
    fun uploadImage(file: File, email: String, tempat: String ,deskripsi: String, latitude: String, longitude: String): LiveData<Result<UploadResponse>> = liveData{
        emit(Result.Loading)
        val requestBodyEmail = email.toRequestBody("text/plain".toMediaType())
        val requestBodyTempat = tempat.toRequestBody("text/plain".toMediaType())
        val requestBodyDeskripsi = deskripsi.toRequestBody("text/plain".toMediaType())
        val requestBodyLatitude = latitude.toRequestBody("text/plain".toMediaType())
        val requestBodyLongitude = longitude.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody, requestBodyEmail, requestBodyTempat, requestBodyDeskripsi, requestBodyLatitude, requestBodyLongitude)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    suspend fun saveSession(userModel: UserModel){
        userPreferences.saveSession(userModel)
    }
    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }
    suspend fun logout(){
        userPreferences.logout()
    }
    companion object {
        private var INSTANCE: UserRepository? = null
        fun clearInstance(){
            INSTANCE = null
        }
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): UserRepository =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: UserRepository(apiService, userPreferences)
        }.also { INSTANCE = it }
    }
}