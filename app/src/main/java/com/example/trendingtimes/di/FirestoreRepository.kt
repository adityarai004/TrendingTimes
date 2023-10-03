package com.example.trendingtimes.di

import androidx.lifecycle.MutableLiveData
import com.example.trendingtimes.data.News
import com.google.firebase.auth.FirebaseUser

interface FirestoreRepository {

    fun addNews(news: News,user: FirebaseUser)
    fun deleteNews(news: News,user: FirebaseUser)
    fun getNews(user: FirebaseUser): MutableLiveData<List<News>>
}