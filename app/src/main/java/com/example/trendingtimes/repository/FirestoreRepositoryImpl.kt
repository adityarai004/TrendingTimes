package com.example.trendingtimes.repository

import com.example.trendingtimes.model.local.News
import com.google.firebase.auth.FirebaseAuth
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

    override suspend fun addNewsToHistory(news: News, user: FirebaseUser) {
        val usersCollection = firebaseFirestore.collection("users")
        val userDoc = usersCollection.document(user.uid)

        userDoc.collection("history")
            .add(news).await()
    }

    override suspend fun deleteNews(news: News, user: FirebaseUser) {
        val userCollection = firebaseFirestore.collection("users")
        val userDoc = userCollection.document(user.uid)
        news.id?.let { userDoc.collection("news").document(it) }?.delete()?.await()
    }

    override suspend fun deleteHistory(news: News, user: FirebaseUser) {
        val userCollection = firebaseFirestore.collection("users")
        val userDoc = userCollection.document(user.uid)
        news.id?.let { userDoc.collection("history").document(it) }?.delete()?.await()
    }

    override fun observeNewsList(
        onSuccess: (List<News>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            firebaseFirestore.collection("users")
                .document(uid)
                .collection("news")
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        onError(error)
                        return@addSnapshotListener
                    }

                    val newsList = mutableListOf<News>()
                    querySnapshot?.let { snapshot ->
                        for (document in snapshot.documents) {
                            val newsId = document.id
                            val newsData = document.data

                            val news = News(
                                id = newsId,
                                title = newsData?.get("title") as String,
                                publishedAt = newsData["publishedAt"] as String,
                                urlImage = newsData["urlImage"] as String,
                                url = newsData["url"] as String
                            )
                            newsList.add(news)
                        }
                        onSuccess(newsList)
                    }
                }
        }
    }

    override fun observeHistoryNewsList(
        onSuccess: (List<News>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            firebaseFirestore.collection("users")
                .document(uid)
                .collection("history")
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        onError(error)
                        return@addSnapshotListener
                    }

                    val newsList = mutableListOf<News>()
                    querySnapshot?.let { snapshot ->
                        for (document in snapshot.documents) {
                            val newsId = document.id
                            val newsData = document.data

                            val news = News(
                                id = newsId,
                                title = newsData?.get("title") as String,
                                publishedAt = newsData["publishedAt"] as String,
                                urlImage = newsData["urlImage"] as String,
                                url = newsData["url"] as String
                            )
                            newsList.add(news)
                        }
                        onSuccess(newsList)
                    }
                }
        }
    }
}