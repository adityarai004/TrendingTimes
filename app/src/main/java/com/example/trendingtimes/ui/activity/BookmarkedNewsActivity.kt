package com.example.trendingtimes.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkedNewsActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsData: MutableList<Article>
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarked_news)
        toolbar = findViewById(R.id.bookmark_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Bookmarks"

        val rv = findViewById<RecyclerView>(R.id.bookmarked_recyclerView)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        newsData = mutableListOf()
        val adapter = NewsAdapter(this, newsData, viewModel)
        rv.adapter = adapter

        viewModel.getNewsFromDB(applicationContext)?.observe(this) {
            newsData.clear()
            it?.let { newsData.addAll(it) }
        }

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

                            viewModel.deleteNews(context = this@BookmarkedNewsActivity,newsData[viewHolder.adapterPosition])
                            rv.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
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
}
