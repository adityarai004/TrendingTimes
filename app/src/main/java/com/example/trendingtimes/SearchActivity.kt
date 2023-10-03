package com.example.trendingtimes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.api.ApiService
import com.example.trendingtimes.api.RetrofitInstance
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.databinding.ActivitySearchBinding
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.ui.adapters.SearchNewsAdapter
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.create

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    var queryNews = mutableListOf<Article>()
    lateinit var viewModel: NewsViewModel
    private lateinit var adapter: SearchNewsAdapter

    private var retroService = RetrofitInstance.retrofit
    private var apiService = RetrofitInstance.api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchRv.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        binding.progressBar.visibility = View.VISIBLE

//        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        val query = intent.getStringExtra("query")

        binding.searchedForTv.text = "$query In Search"

        if (query != null) {
            GlobalScope.launch(Dispatchers.IO) {
                val response = apiService.getEverythingNews(query)
                if(response.isSuccessful){
                    queryNews.clear()
                    Log.d("TAG","Response successful")
                    response.body()?.articles?.let { queryNews.addAll(it) }
                    Log.d("TAG","${queryNews.size}")
                    adapter = SearchNewsAdapter(applicationContext,queryNews)
                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        adapter = SearchNewsAdapter(applicationContext, queryNews)
                        binding.searchRv.adapter = adapter
                    }
                }
            }
        }

//        setupRecyclerView()
        if(NetworkUtils.isNetworkAvailable(this)){
            if (query != null) {
                Log.d("Search","query is not null")
//                fetchAllNews(query)
            }
        }
        else{
            Toast.makeText(this,"No Internet Connection Detected", Toast.LENGTH_SHORT).show()
        }

//        if (query != null) {
//            viewModel.fetchQueryNews(query)
//        }


        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                queryNews.clear()
                // Back is pressed... Finishing the activity
                finish()
            }
        })

    }

//    private fun fetchAllNews(query:String) {
//        viewModel.fetchQueryNews(query).observe(this, Observer {
//            if(it != null){
//                queryNews.addAll(it.articles)
//                adapter = SearchNewsAdapter(applicationContext,queryNews)
//                adapter.setList(it.articles.toMutableList())
//                binding.searchRv.adapter = adapter
//            }
//        })
//    }

    private fun setupRecyclerView() {
        adapter = SearchNewsAdapter(applicationContext, queryNews)
        binding.searchRv.adapter = adapter
    }

    override fun onResume() {
//        intent.getStringExtra("query")?.let { fetchAllNews(it) }
        super.onResume()
    }
}
