package com.example.trendingtimes.auth.di

import com.example.trendingtimes.auth.data.remote.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}