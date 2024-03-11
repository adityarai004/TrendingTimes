package com.example.trendingtimes.repository


import com.example.trendingtimes.api.ApiService
import com.example.trendingtimes.data.News
import com.example.trendingtimes.data.NewsResponse
import com.example.trendingtimes.db.ArticleDatabase
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val apiService: ApiService,
                                         private val localDB: ArticleDatabase) {
    suspend fun getNewsWithQuery(q: String,page: Int): Response<NewsResponse> = apiService.getEverythingNews(q, pageNumber = page)
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