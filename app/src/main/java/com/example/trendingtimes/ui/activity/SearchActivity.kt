package com.example.trendingtimes.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.data.News
import com.example.trendingtimes.databinding.ActivitySearchBinding
import com.example.trendingtimes.ui.adapters.AdapterInterface
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding

    @Inject
    lateinit var viewModel: NewsViewModel

    private var list = mutableListOf<Article>()
    var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchRv.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        binding.progressBar.visibility = View.VISIBLE

        val query = intent.getStringExtra("query")

        binding.searchedForTv.text = "$query In Search"

        if (query != null) {
            viewModel.searchNews(query)
        }

        viewModel.searchNewsResponse.observe(this){
            if(it.articles.isNotEmpty()){
                binding.progressBar.visibility = View.GONE
                list.clear()
                list.addAll(it.articles)
                val adapter = NewsAdapter(this,list,object : AdapterInterface {
                    override fun didLongPress(news: News) {
                        viewModel.insertNews(news,
                            onSuccess
                            = {
                                Toast.makeText(this@SearchActivity,"News bookmarked successfully",Toast.LENGTH_LONG).show()
                            },
                            onError = {
                                Toast.makeText(this@SearchActivity,"Unable to bookmark news at the moment.",Toast.LENGTH_LONG).show()
                            })
                    }

                    override fun endOfList() {

                    }
                })
                binding.progressBar.visibility = View.GONE
                binding.searchRv.adapter = adapter
            }
        }

        if(NetworkUtils.isNetworkAvailable(this)){
            if (query != null) {
                Log.d("Search","query is not null")
            }
        }
        else{
            Toast.makeText(this,"No Internet Connection Detected", Toast.LENGTH_SHORT).show()
        }
    }
}
