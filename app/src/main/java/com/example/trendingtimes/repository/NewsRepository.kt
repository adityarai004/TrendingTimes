package com.example.trendingtimes.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.trendingtimes.api.ApiService
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.data.NewsResponse
import com.example.trendingtimes.db.ArticleDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class NewsRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getNewsWithQuery(q: String): Response<NewsResponse> = apiService.getEverythingNews(q)

    companion object {

        private var articleDatabase: ArticleDatabase? = null

        private fun initializeDB(context: Context): ArticleDatabase {
            return ArticleDatabase.getDatabase(context)
        }

        fun insertArticle(context: Context, article: Article) {

            articleDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                articleDatabase!!.articleDao().insertArticle(article)
            }
        }

        fun deleteArticle(context: Context, article: Article) {

            articleDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                articleDatabase!!.articleDao().deleteArticle(article)
            }
        }

        suspend fun getAllNews(context: Context): LiveData<List<Article>> {
            articleDatabase = initializeDB(context)
            return articleDatabase!!.articleDao().getAllArticles()
        }

    }
}