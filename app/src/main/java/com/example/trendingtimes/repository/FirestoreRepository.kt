package com.example.trendingtimes.repository

import androidx.lifecycle.MutableLiveData
import com.example.trendingtimes.data.News
import com.google.firebase.auth.FirebaseUser

interface FirestoreRepository {

    suspend fun addNews(news: News,user: FirebaseUser)
    suspend fun deleteNews(news: News,user: FirebaseUser)
    fun getNews(user: FirebaseUser): MutableLiveData<List<News>>
}
