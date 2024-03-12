package com.example.trendingtimes.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingtimes.R
import com.example.trendingtimes.model.local.News
import com.example.trendingtimes.databinding.ActivityBookmarkedNewsBinding
import com.example.trendingtimes.ui.adapters.BookmarkNewsAdapter
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkedNewsActivity : AppCompatActivity() {
    private var newsData = mutableListOf<News>()
    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityBookmarkedNewsBinding
    private lateinit var adapter: BookmarkNewsAdapter

    @Inject
    lateinit var viewModel: NewsViewModel
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
        binding.bookmarkedRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        loadData()

        if (NetworkUtils.isNetworkAvailable(this)) {
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

                            viewModel.deleteNews(newsData[viewHolder.adapterPosition], success = {
                                if (it) {
                                    Toast.makeText(
                                        this@BookmarkedNewsActivity,
                                        "Bookmarked news deleted successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }, onError = {
                                Toast.makeText(
                                    this@BookmarkedNewsActivity,
                                    "Exception $it occurred",
                                    Toast.LENGTH_LONG
                                ).show()
                            })
                        }
                    }

                    override fun clearView(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder
                    ) {
                        super.clearView(recyclerView, viewHolder)
                        rightSwipeActivated = false
                    }
                }
            )
            mIth.attachToRecyclerView(rv)
        }

    }

    private fun loadData() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            loadDataFromFirestore()
        } else {
            loadDataFromLocal()
        }
    }

    private fun loadDataFromFirestore() {
        viewModel.getNewsFromRemote()
        viewModel.bookmarkedNews.observe(this) {
            onSuccess(it)
        }
    }

    private fun loadDataFromLocal() {
        viewModel.getNewsFromDB()
        viewModel.bookmarkedNews.observe(this) { articles ->
            onSuccess(articles)
        }
    }

    private fun onSuccess(newsList: List<News>) {
        newsData.clear()
        newsData.addAll(newsList)
        adapter = BookmarkNewsAdapter(this, newsData)
        binding.bookmarkedRecyclerView.adapter = adapter
    }
}
