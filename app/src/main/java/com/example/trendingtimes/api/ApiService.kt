package com.example.trendingtimes.api

import com.example.trendingtimes.data.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
     @GET("news/{category}")
     suspend fun getEverythingNews(
         @Path("category") category:String,
         @Query("page") pageNumber: Int = 1,
         @Query("limit") limit:Int = 20
     ): Response<NewsResponse>


    @GET("search/{keyword}")
    suspend fun searchNews(
        @Path("keyword") keyword:String
    ): Response<NewsResponse>
}