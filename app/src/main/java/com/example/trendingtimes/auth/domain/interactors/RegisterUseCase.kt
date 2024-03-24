package com.example.trendingtimes.auth.domain.interactors

import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.domain.repository.AuthRepository
import com.example.trendingtimes.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(registerBody: AuthDTO) = authRepository.register(registerBody)

}