package com.example.trendingtimes.repository

import com.example.trendingtimes.data.News
import com.google.firebase.auth.FirebaseUser

interface FirestoreRepository {

    suspend fun addNews(news: News,user: FirebaseUser)
    suspend fun deleteNews(news: News,user: FirebaseUser)
    fun observeNewsList(
        onSuccess: (List<News>) -> Unit,
        onError: (Exception) -> Unit
    )
}
