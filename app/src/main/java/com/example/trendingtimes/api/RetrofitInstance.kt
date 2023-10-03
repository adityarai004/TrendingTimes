package com.example.trendingtimes.api

import com.example.trendingtimes.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.create

class RetrofitInstance {

    companion object {
        val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(logging).build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val api by lazy{
            retrofit.create(ApiService::class.java)
        }

    }
}