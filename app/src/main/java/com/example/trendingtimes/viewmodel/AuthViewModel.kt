package com.example.trendingtimes.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingtimes.model.local.News
import com.example.trendingtimes.util.Resource
import com.example.trendingtimes.repository.AuthRepository
import com.example.trendingtimes.repository.FirestoreRepository
import com.example.trendingtimes.repository.NewsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _bookmarkedNews = MutableLiveData<List<News>>()
    val bookmarkedNews = _bookmarkedNews
    suspend fun login(email: String, password: String): Boolean {
        val result = authRepository.login(email, password)
        return result is Resource.Success
    }

    suspend fun signUp(email: String, password: String): Boolean {
        val result = authRepository.signup(email, password)
        return result is Resource.Success
    }

    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    fun logOut() {
        authRepository.logout()
    }


    fun insertNews(news: News, onSuccess: () -> Unit, onError: (exception: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getCurrentUser()?.let { firestoreRepository.addNews(news, it) }

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

    //Common function to delete bookmark news locally and remotely
    fun deleteNews(news: News, success: (Boolean) -> Unit, onError: (exception: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestoreRepository.deleteNews(news, FirebaseAuth.getInstance().currentUser!!)

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

    fun doNothing() {
        Log.d("Nothing", "Doing nothing!!!!!!!!!")
    }
}