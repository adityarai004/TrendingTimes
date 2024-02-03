package com.example.trendingtimes.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.data.NewsResponse
import com.example.trendingtimes.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {

    private val _businessNewsResponse = MutableLiveData<NewsResponse>()
    val businessNewsResponse = _businessNewsResponse

    private val _entertainmentNewsResponse = MutableLiveData<NewsResponse>()
    val entertainmentNewsResponse = _entertainmentNewsResponse

    private val _scienceNewsResponse = MutableLiveData<NewsResponse>()
    val scienceNewsResponse = _scienceNewsResponse

    private val _educationNewsResponse = MutableLiveData<NewsResponse>()
    val educationNewsResponse = _educationNewsResponse

    private val _technologyNewsResponse = MutableLiveData<NewsResponse>()
    val technologyNewsResponse = _technologyNewsResponse

    private val _opinionNewsResponse = MutableLiveData<NewsResponse>()
    val opinionNewsResponse = _opinionNewsResponse

    private val _topHeadlinesNewsResponse = MutableLiveData<NewsResponse>()
    val topHeadlinesNewsResponse = _topHeadlinesNewsResponse

    private val _healthNewsResponse = MutableLiveData<NewsResponse>()
    val healthNewsResponse = _healthNewsResponse

    private val _sportsNewsResponse = MutableLiveData<NewsResponse>()
    val sportsNewsResponse = _sportsNewsResponse

    private val _searchNewsResponse = MutableLiveData<NewsResponse>()
    val searchNewsResponse = _searchNewsResponse

    fun fetchNews(category: String, query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = newsRepository.getNewsWithQuery(query)
            when (category) {
                "entertainment" -> _entertainmentNewsResponse.postValue(response.body())
                "health" -> _healthNewsResponse.postValue(response.body())
                "science" -> _scienceNewsResponse.postValue(response.body())
                "sports" -> _sportsNewsResponse.postValue(response.body())
                "education" -> _educationNewsResponse.postValue(response.body())
                "top_headlines" -> _topHeadlinesNewsResponse.postValue(response.body())
                "technology" -> _technologyNewsResponse.postValue(response.body())
                "opinion" -> _opinionNewsResponse.postValue(response.body())
                "business" -> _businessNewsResponse.postValue(response.body())
                "search" -> _searchNewsResponse.postValue(response.body())
            }
            if (!response.isSuccessful) {
                Log.d("TAG", "Limit reached")
            }
        }
    }


    // Local Database
    var articleData: LiveData<List<Article>>? = null

    fun insertNews(context: Context, news: Article) {
        NewsRepository.insertArticle(context, news)
    }

    fun deleteNews(context: Context, news: Article) {
        NewsRepository.deleteArticle(context, news)
    }


    fun getNewsFromDB(context: Context): LiveData<List<Article>>? {
        viewModelScope.launch(Dispatchers.IO) {
            articleData = NewsRepository.getAllNews(context)
        }
        return articleData
    }

//    fun getNewsFromFirebase()
}