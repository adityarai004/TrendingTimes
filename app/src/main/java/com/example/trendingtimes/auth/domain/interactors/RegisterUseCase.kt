package com.example.trendingtimes.auth.domain.interactors

import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.domain.repository.AuthRepository
import com.example.trendingtimes.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke(registerBody: AuthDTO) = flow {
        emit(Resource.Loading)
        try {
            val response = authRepository.register(registerBody)
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