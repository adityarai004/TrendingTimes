package com.example.trendingtimes.repository

import androidx.lifecycle.MutableLiveData
import com.example.trendingtimes.data.News
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) :
    FirestoreRepository {
    override fun addNews(news: News, user: FirebaseUser) {
        val usersCollection = firebaseFirestore.collection("users")
        val userDoc = usersCollection.document(user.uid)

        val newsData = hashMapOf("newsTitle" to news.title,
            "publishedAt" to news.publishedAt,
            "url" to news.url,
            "urlImage" to news.urlImage)

        userDoc.collection("news")
            .add(newsData)
            .addOnSuccessListener {
                val newsId = it?.id
                if (newsId != null) {
                    news.id = newsId
                }
            }
    }

    override fun deleteNews(news: News, user: FirebaseUser) {
        val userCollection = firebaseFirestore.collection("users")
        val userDoc = userCollection.document(user.uid)
        news.id?.let { userDoc.collection("tasks").document(it) }?.delete()
    }

    override fun getNews(user: FirebaseUser): MutableLiveData<List<News>> {
        TODO("Not yet implemented")
    }
}