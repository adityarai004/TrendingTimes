package com.example.trendingtimes.auth.data.repository

import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.data.model.AuthRespDTO
import com.example.trendingtimes.auth.data.remote.AuthService
import com.example.trendingtimes.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) : AuthRepository {

    override suspend fun register(registerBody: AuthDTO): AuthRespDTO {
        return authService.register(registerBody)
    }

    override suspend fun login(loginBody: AuthDTO): AuthRespDTO {
        return authService.login(loginBody)
    }

}