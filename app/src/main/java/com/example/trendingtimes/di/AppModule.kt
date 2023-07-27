package com.example.trendingtimes.di

import com.example.trendingtimes.viewmodel.NewsViewModel
import com.example.trendingtimes.api.ApiService
import com.example.trendingtimes.repository.NewsRepository
import com.example.trendingtimes.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit):ApiService{
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: ApiService):NewsRepository{
        return NewsRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideNewsViewModel(newsRepository: NewsRepository): NewsViewModel {
        return NewsViewModel(newsRepository)
    }
}