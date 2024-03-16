package com.example.trendingtimes.auth.di

import com.example.trendingtimes.auth.data.remote.AuthService
import com.example.trendingtimes.auth.data.repository.AuthRepositoryImpl
import com.example.trendingtimes.auth.domain.interactors.LoginUseCase
import com.example.trendingtimes.auth.domain.interactors.RegisterUseCase
import com.example.trendingtimes.auth.domain.repository.AuthRepository
import com.example.trendingtimes.auth.presentation.ui.AuthViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository{
        return AuthRepositoryImpl(authService)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAuthViewModel(loginUseCase: LoginUseCase,registerUseCase: RegisterUseCase) : AuthViewModel{
        return AuthViewModel(loginUseCase,registerUseCase)
    }

}