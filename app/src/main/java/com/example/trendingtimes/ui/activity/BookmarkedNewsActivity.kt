package com.example.trendingtimes.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.data.News
import com.example.trendingtimes.databinding.ActivityBookmarkedNewsBinding
import com.example.trendingtimes.ui.adapters.BookmarkNewsAdapter
import com.example.trendingtimes.viewmodel.AuthViewModel
import com.example.trendingtimes.viewmodel.NewsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.context.AttributeContext.Auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkedNewsActivity : AppCompatActivity() {
    private var newsData =  mutableListOf<News>()
    private lateinit var toolbar: Toolbar
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding : ActivityBookmarkedNewsBinding
    private lateinit var adapter: BookmarkNewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkedNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.bookmark_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Bookmarks"

        val rv = findViewById<RecyclerView>(R.id.bookmarked_recyclerView)
//        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.bookmarkedRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
//        newsData = mutableListOf()

        loadDataFromFirestore()



        val mIth = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.RIGHT
                ) {
                    private var rightSwipeActivated = false

                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        if (direction == ItemTouchHelper.RIGHT) {
                            rightSwipeActivated = true

                            val db = FirebaseFirestore.getInstance()
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val pos = viewHolder.adapterPosition


                            currentUser?.uid?.let{uid->
                                val newsDocumentRef = newsData[viewHolder.adapterPosition].id?.let {
                                    db.collection("users")
                                        .document(uid) // The user's document
                                        .collection("news") // The "news" subcollection
                                        .document(it)
                                }

                                newsDocumentRef?.delete()?.addOnSuccessListener {
                                    loadDataFromFirestore()
                                    Toast.makeText(applicationContext, "News item deleted", Toast.LENGTH_SHORT).show()
                                }
                            }


//                            viewModel.deleteNews(context = this@BookmarkedNewsActivity,newsData[viewHolder.adapterPosition])
//                            rv.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                        }
                    }

                    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                        super.clearView(recyclerView, viewHolder)
                        rightSwipeActivated = false
                    }
                }
            )
        mIth.attachToRecyclerView(rv)
    }

    private fun loadDataFromFirestore() {
        newsData.clear()
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            db.collection("users")
                .document(uid)
                .collection("news")
                .addSnapshotListener{querySnapshot, error ->
                    if(error!=null){
                        return@addSnapshotListener
                    }

                    val newsList = mutableListOf<News>()
                    querySnapshot?.let { snapshot->
                        for (document in snapshot.documents){
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

    private fun onSuccess(newsList: MutableList<News>) {
        newsData.addAll(newsList)
        adapter = BookmarkNewsAdapter(this, newsData)
        binding.bookmarkedRecyclerView.adapter = adapter
    }
}
