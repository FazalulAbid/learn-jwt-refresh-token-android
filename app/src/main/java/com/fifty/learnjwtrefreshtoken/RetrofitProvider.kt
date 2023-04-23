package com.fifty.learnjwtrefreshtoken

import android.content.SharedPreferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    const val BASE_URL = ""
    const val JWT_TOKEN = "jwt-token"

    private fun getRetrofit(sharedPreferences: SharedPreferences): Retrofit {

        val headerInterceptor = AuthInterceptor(sharedPreferences)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    fun getApiService(sharedPreferences: SharedPreferences): ApiService =
        getRetrofit(sharedPreferences).create(ApiService::class.java)

    fun getTokenApiService(): TokenApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(TokenApiService::class.java)
    }
}