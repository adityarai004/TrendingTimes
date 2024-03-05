package com.example.trendingtimes.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.RoomDatabase
import com.example.trendingtimes.api.ApiService
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.data.News
import com.example.trendingtimes.data.NewsResponse
import com.example.trendingtimes.db.ArticleDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class NewsRepository @Inject constructor(private val apiService: ApiService,
                                         private val localDB: ArticleDatabase) {
    suspend fun getNewsWithQuery(q: String): Response<NewsResponse> = apiService.getEverythingNews(q)
    suspend fun insertArticle(news: News) {
        localDB.articleDao().insertArticle(news)
    }

    suspend fun deleteArticle(news: News) {
        localDB.articleDao().deleteArticle(news)
    }

    fun getAllNews(): List<News> {
        return localDB.articleDao().getAllArticles()
    }
}