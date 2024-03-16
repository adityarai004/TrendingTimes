package com.example.trendingtimes.di

import android.content.Context
import androidx.room.Room
import com.example.trendingtimes.viewmodel.NewsViewModel
import com.example.trendingtimes.api.ApiService
import com.example.trendingtimes.db.ArticleDatabase
import com.example.trendingtimes.repository.AuthRepository
import com.example.trendingtimes.repository.AuthRepositoryImpl
import com.example.trendingtimes.repository.FirestoreRepository
import com.example.trendingtimes.repository.FirestoreRepositoryImpl
import com.example.trendingtimes.repository.NewsRepository
import com.example.trendingtimes.util.Constants.Companion.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        apiService: ApiService,
        articleDatabase: ArticleDatabase
    ): NewsRepository {
        return NewsRepository(apiService, articleDatabase)
    }

    @Singleton
    @Provides
    fun provideNewsViewModel(
        newsRepository: NewsRepository,
        firestoreRepository: FirestoreRepository
    ): NewsViewModel {
        return NewsViewModel(newsRepository, firestoreRepository)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirestoreRepository(impl: FirestoreRepositoryImpl): FirestoreRepository = impl

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(context, ArticleDatabase::class.java, "article").build()
    }
}