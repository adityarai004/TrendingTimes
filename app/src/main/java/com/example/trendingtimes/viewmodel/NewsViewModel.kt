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
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository):
    ViewModel(){

    private val _businessNewsResponse = MutableLiveData<NewsResponse>()
    val businessNewsResponse =  _businessNewsResponse
    private val _entertainmentNewsResponse = MutableLiveData<NewsResponse>()
    val entertainmentNewsResponse =  _entertainmentNewsResponse
private val _scienceNewsResponse = MutableLiveData<NewsResponse>()
    val scienceNewsResponse =  _scienceNewsResponse
private val _educationNewsResponse = MutableLiveData<NewsResponse>()
    val educationNewsResponse =  _educationNewsResponse
private val _technologyNewsResponse = MutableLiveData<NewsResponse>()
    val technologyNewsResponse =  _technologyNewsResponse
private val _opinionNewsResponse = MutableLiveData<NewsResponse>()
    val opinionNewsResponse =  _opinionNewsResponse
private val _topHeadlinesNewsResponse = MutableLiveData<NewsResponse>()
    val topHeadlinesNewsResponse =  _topHeadlinesNewsResponse
private val _healthNewsResponse = MutableLiveData<NewsResponse>()
    val healthNewsResponse =  _healthNewsResponse
private val _sportsNewsResponse = MutableLiveData<NewsResponse>()
    val sportsNewsResponse =  _sportsNewsResponse

    fun fetchBusinessNews():MutableLiveData<NewsResponse>{
        viewModelScope.launch(Dispatchers.IO) {
            val response = newsRepository.getNewsWithQuery("business")
            if (response.isSuccessful) {
                _businessNewsResponse.postValue(response.body())

            }
            else{
                Log.d("TAG","Limit reached")
            }
        }
        return businessNewsResponse
    }
    fun fetchEntertainmentNews(q:String):MutableLiveData<NewsResponse>{
        viewModelScope.launch(Dispatchers.IO){
            val response = newsRepository.getNewsWithQuery(q)
            if(response.isSuccessful){
                _entertainmentNewsResponse.postValue(response.body())
            }
            else{
                Log.d("TAG","Limit reached")
            }
        }
        return entertainmentNewsResponse
    }
    fun fetchHealthNews(q:String):MutableLiveData<NewsResponse>{
        viewModelScope.launch(Dispatchers.IO){
            val response = newsRepository.getNewsWithQuery(q)
            if(response.isSuccessful){
                _healthNewsResponse.postValue(response.body())
            }
            else{
                Log.d("TAG","Limit reached")
            }
        }
        return healthNewsResponse
    }
    fun fetchScienceNews(q:String):MutableLiveData<NewsResponse>{
        viewModelScope.launch(Dispatchers.IO){
            val response = newsRepository.getNewsWithQuery(q)
            if(response.isSuccessful){
                _scienceNewsResponse.postValue(response.body())
            }
            else{
                Log.d("TAG","Limit reached")
            }        }
        return scienceNewsResponse
    }
    fun fetchSportsNews(q:String):MutableLiveData<NewsResponse>{
        viewModelScope.launch(Dispatchers.IO){
            val response = newsRepository.getNewsWithQuery(q)
            if(response.isSuccessful){
                _sportsNewsResponse.postValue(response.body())
            }
            else{
                Log.d("TAG","Limit reached")
            }        }
        return sportsNewsResponse
    }
    fun fetchEducationNews(q:String):MutableLiveData<NewsResponse>{
        viewModelScope.launch(Dispatchers.IO){
            val response = newsRepository.getNewsWithQuery(q)
            if(response.isSuccessful){
                _educationNewsResponse.postValue(response.body())
            }
            else{
                Log.d("TAG","Limit reached")
            }        }
        return educationNewsResponse
    }
    fun fetchTopHeadlinesNews(q:String):MutableLiveData<NewsResponse>{
        viewModelScope.launch(Dispatchers.IO){
            val response = newsRepository.getNewsWithQuery(q)
            if(response.isSuccessful){
                _topHeadlinesNewsResponse.postValue(response.body())
            }
            else{
                Log.d("TAG","Limit reached")
            }        }
        return topHeadlinesNewsResponse
    }
    fun fetchTechnologyNews(q:String):MutableLiveData<NewsResponse>{
        viewModelScope.launch(Dispatchers.IO){
            val response = newsRepository.getNewsWithQuery(q)
            if(response.isSuccessful){
                _technologyNewsResponse.postValue(response.body())
            }
            else{
                Log.d("TAG","Limit reached")
            }        }
        return technologyNewsResponse
    }
    fun fetchOpinionNews(q:String):MutableLiveData<NewsResponse>{
        viewModelScope.launch(Dispatchers.IO){
            val response = newsRepository.getNewsWithQuery(q)
            if(response.isSuccessful){
                _opinionNewsResponse.postValue(response.body())
            }
            else{
                Log.d("TAG","Limit reached")
            }        }
        return opinionNewsResponse
    }


    var articleData: LiveData<List<Article>>? = null

    fun insertNews(context: Context, news: Article) {
        NewsRepository.insertArticle(context, news)
    }

    fun deleteNews(context: Context, news: Article) {
        NewsRepository.deleteArticle(context, news)
    }


    fun getNewsFromDB(context: Context): LiveData<List<Article>>? {
        viewModelScope.launch (Dispatchers.IO){
            articleData = NewsRepository.getAllNews(context)
        }
        return articleData
    }
}