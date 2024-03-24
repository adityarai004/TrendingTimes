package com.example.trendingtimes.auth.data.repository

import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.data.model.AuthRespDTO
import com.example.trendingtimes.auth.data.remote.AuthService
import com.example.trendingtimes.auth.domain.repository.AuthRepository
import com.example.trendingtimes.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) : AuthRepository {

    override suspend fun register(registerBody: AuthDTO) =  flow {
        emit(Resource.Loading)
        try {
            val response = authService.register(registerBody)
            if (response.success) {
                emit(Resource.Success(response.message))
            } else {
                emit(Resource.Failure(response.message))
            }
        } catch (e: HttpException) {
            emit(Resource.Failure(e.message))
        } catch (e: Exception) {
            emit(Resource.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun login(loginBody: AuthDTO) = flow {
        emit(Resource.Loading)
        try {
            val response = authService.login(loginBody)
            if (response.success) {
                emit(Resource.Success(response.message))
            } else {
                emit(Resource.Failure(response.message))
            }
        } catch (e: HttpException) {
            emit(Resource.Failure(e.message))
        } catch (e: Exception) {
            emit(Resource.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

}