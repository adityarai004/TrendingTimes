package com.example.trendingtimes.auth.domain.interactors

import android.util.Log
import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.domain.repository.AuthRepository
import com.example.trendingtimes.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository){

    operator fun invoke(loginBody: AuthDTO) = flow {
        emit(Resource.Loading)
        try {
            val response = authRepository.login(loginBody)
            if (response.success){
                Log.d("Login","success : ${response.message}")
                emit(Resource.Success(response.message))
            }
            else{
                Log.d("Login","success : ${response.message}")
                emit(Resource.Failure(response.message))
            }
        } catch (e: HttpException){
            Log.d("Login","success : ${e.message}")
            emit(Resource.Failure(e.message))
        } catch (e: Exception){
            Log.d("Login","success : ${e.message}")
            emit(Resource.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

}