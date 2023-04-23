package com.fifty.learnjwtrefreshtoken

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {

    private fun refreshToken(chain: Interceptor.Chain, request: Request): Response {
        val response = RetrofitProvider.getTokenApiService().createNewToken().execute()
        return if (response.isSuccessful) {
            val token = response.body()?.token

            sharedPreferences
                .edit()
                .putString(RetrofitProvider.JWT_TOKEN, token)
                .apply()

            val newRequest = request
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            // Make original request with new token.
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString(RetrofitProvider.JWT_TOKEN, null)
        val request = chain.request()
        if (!token.isNullOrEmpty()) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            val response = chain.proceed(newRequest)
            return if (response.code() == 401) {
                // Unauthorized access
                response.close()
                refreshToken(chain, request)
            } else {
                response
            }
        } else {
            return refreshToken(chain, chain.request())
        }
    }
}