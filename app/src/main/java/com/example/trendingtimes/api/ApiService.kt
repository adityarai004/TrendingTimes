package com.example.trendingtimes.api

import com.example.trendingtimes.model.remote.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Endpoint for default tab news
     @GET("news/{category}")
     suspend fun getEverythingNews(
         @Path("category") category:String,
         @Query("page") pageNumber: Int = 1,
         @Query("limit") limit:Int = 20
     ): Response<NewsResponse>

     // Endpoint for searching news
    @GET("search/{keyword}")
    suspend fun searchNews(
        @Path("keyword") keyword:String
    ): Response<NewsResponse>
}