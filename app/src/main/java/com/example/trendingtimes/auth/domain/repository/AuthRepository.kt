package com.example.trendingtimes.auth.domain.repository

import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.data.model.AuthRespDTO

interface AuthRepository{

    suspend fun register(registerBody: AuthDTO) : AuthRespDTO
    suspend fun login(loginBody: AuthDTO): AuthRespDTO

}