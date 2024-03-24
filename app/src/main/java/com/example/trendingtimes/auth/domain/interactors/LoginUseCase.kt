package com.example.trendingtimes.auth.domain.interactors

import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository){

    suspend operator fun invoke(loginBody: AuthDTO) = authRepository.login(loginBody)

}