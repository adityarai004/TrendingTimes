package com.example.trendingtimes.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingtimes.data.News
import com.example.trendingtimes.data.NewsResponse
import com.example.trendingtimes.repository.FirestoreRepository
import com.example.trendingtimes.repository.NewsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository,private val firestoreRepository: FirestoreRepository) :
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

    private val _bookmarkedNews  = MutableLiveData<List<News>>()
    val bookmarkedNews = _bookmarkedNews
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
    fun deleteNews(news: News, success: (Boolean) -> Unit,onError: (exception: String) -> Unit,) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                firestoreRepository.deleteNews(news,FirebaseAuth.getInstance().currentUser!!)

                newsRepository.deleteArticle(news)

                withContext(Dispatchers.Main){
                    success(true)
                }
            } catch (e: Exception){
                Log.e("ERROR","Something went wrong $e")
                withContext(Dispatchers.Main){
                    e.message?.let { onError(it) }
                }
            }
        }
    }
    fun insertNews(news: News, onSuccess: () -> Unit,onError: (exception : String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreRepository.addNews(news,FirebaseAuth.getInstance().currentUser!!)

                newsRepository.insertArticle(news)
                withContext(Dispatchers.Main){
                    onSuccess()
                }
            } catch (e: Exception){
                Log.e("ERROR","Something went wrong $e")
                withContext(Dispatchers.Main){
                    e.message?.let { onError(it) }
                }
            }
        }
    }

    fun getNewsFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            _bookmarkedNews.postValue(newsRepository.getAllNews())
        }
    }

    fun getNewsFromRemote(){
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.observeNewsList(onSuccess = {
                _bookmarkedNews.postValue(it)
            },
                onError = {

                })
        }
    }
}