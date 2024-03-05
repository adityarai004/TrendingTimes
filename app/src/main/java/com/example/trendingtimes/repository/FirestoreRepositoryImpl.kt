package com.example.trendingtimes.repository

import androidx.lifecycle.MutableLiveData
import com.example.trendingtimes.data.News
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) :
    FirestoreRepository {
    override suspend fun addNews(news: News, user: FirebaseUser) {
        val usersCollection = firebaseFirestore.collection("users")
        val userDoc = usersCollection.document(user.uid)

        userDoc.collection("news")
            .add(news).await()
    }

    override suspend fun deleteNews(news: News, user: FirebaseUser) {
        val userCollection = firebaseFirestore.collection("users")
        val userDoc = userCollection.document(user.uid)
        news.id?.let { userDoc.collection("news").document(it) }?.delete()?.await()
    }

    override fun getNews(user: FirebaseUser): MutableLiveData<List<News>> {
        TODO("Not yet implemented")
    }
}