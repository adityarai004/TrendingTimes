package com.example.trendingtimes.auth.data.remote

import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.data.model.AuthRespDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/register")
    suspend fun register(@Body registerBody: AuthDTO): AuthRespDTO

    @POST("auth/login")
    suspend fun login(@Body loginBody: AuthDTO): AuthRespDTO

}