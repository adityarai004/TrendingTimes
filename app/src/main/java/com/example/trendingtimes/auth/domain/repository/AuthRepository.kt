package com.example.trendingtimes.auth.domain.repository

import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.data.model.AuthRespDTO
import com.example.trendingtimes.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository{

    suspend fun register(registerBody: AuthDTO): Flow<Resource<String>>

    suspend fun login(loginBody: AuthDTO): Flow<Resource<String>>

}