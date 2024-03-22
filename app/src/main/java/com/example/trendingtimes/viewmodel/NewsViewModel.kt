package com.example.trendingtimes.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingtimes.model.local.News
import com.example.trendingtimes.model.remote.NewsResponse
import com.example.trendingtimes.repository.FirestoreRepository
import com.example.trendingtimes.repository.NewsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val firestoreRepository: FirestoreRepository
) :
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

    private val _bookmarkedNews = MutableLiveData<List<News>>()
    val bookmarkedNews = _bookmarkedNews

    private val _historyNews = MutableLiveData<List<News>>()
    val historyNews = _historyNews
    fun fetchNews(query: String, page: Int) {
        // the below line is used to do something on the background thread
        viewModelScope.launch(Dispatchers.IO) {
            val response = newsRepository.getNewsWithQuery(query, page)
            if (response.isSuccessful) {
                when (query) {
                    "entertainment" -> _entertainmentNewsResponse.postValue(response.body())
                    "health" -> _healthNewsResponse.postValue(response.body())
                    "science" -> _scienceNewsResponse.postValue(response.body())
                    "sports" -> _sportsNewsResponse.postValue(response.body())
                    "education" -> _educationNewsResponse.postValue(response.body())
                    "Top Headlines" -> _topHeadlinesNewsResponse.postValue(response.body())
                    "technology" -> _technologyNewsResponse.postValue(response.body())
                    "opinion" -> _opinionNewsResponse.postValue(response.body())
                    "business" -> _businessNewsResponse.postValue(response.body())
                }
            } else {
                Log.d("TAG", "Limit reached")
            }
        }
    }

    fun searchNews(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = newsRepository.getSearchNews(keyword)
            if (response.isSuccessful) {
                _searchNewsResponse.postValue(response.body())
            } else {
                Log.d("TAG", "Search news error")
            }
        }
    }

    //Common function to delete bookmark news locally and remotely
    fun deleteNews(news: News, success: (Boolean) -> Unit, onError: (exception: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreRepository.deleteNews(news, FirebaseAuth.getInstance().currentUser!!)

                //locally deleting news from db
                newsRepository.deleteArticle(news)

                withContext(Dispatchers.Main) {
                    success(true)
                }
            } catch (e: Exception) {
                Log.e("ERROR", "Something went wrong $e")
                withContext(Dispatchers.Main) {
                    e.message?.let { onError(it) }
                }
            }
        }
    }

    //Common function to bookmark a news locally and remotely
    fun insertNews(news: News, onSuccess: () -> Unit, onError: (exception: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreRepository.addNews(news, FirebaseAuth.getInstance().currentUser!!)

                newsRepository.insertArticle(news)
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("ERROR", "Something went wrong $e")
                withContext(Dispatchers.Main) {
                    e.message?.let { onError(it) }
                }
            }
        }
    }

    fun addNewsToHistory(news: News, onSuccess: () -> Unit, onError: (exception: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreRepository.addNewsToHistory(news, FirebaseAuth.getInstance().currentUser!!)
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("ERROR", "Something went wrong $e")
                withContext(Dispatchers.Main) {
                    e.message?.let { onError(it) }
                }
            }
        }
    }

    fun deleteHistory(news: News, success: (Boolean) -> Unit, onError: (exception: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreRepository.deleteHistory(news, FirebaseAuth.getInstance().currentUser!!)


                withContext(Dispatchers.Main) {
                    success(true)
                }
            } catch (e: Exception) {
                Log.e("ERROR", "Something went wrong $e")
                withContext(Dispatchers.Main) {
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

    fun getNewsFromRemote() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.observeNewsList(onSuccess = {
                _bookmarkedNews.postValue(it)
            },
                onError = {

                })
        }
    }

    fun getHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.observeHistoryNewsList(onSuccess = {
                _historyNews.postValue(it)
            },
                onError = {

                })
        }
    }
}