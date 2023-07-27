package com.example.trendingtimes.api

import com.example.trendingtimes.data.NewsResponse
import com.example.trendingtimes.util.Constants.Companion.API_KEY
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
     @GET("everything")
     suspend fun getEverythingNews(
         @Query("q") q:String,
         @Query("apiKey") apiKey:String = API_KEY,
         @Query("page") pageNumber: Int = 1,
         @Query("sortBy") sortBy:String = "publishedAt"
     ): Response<NewsResponse>

}