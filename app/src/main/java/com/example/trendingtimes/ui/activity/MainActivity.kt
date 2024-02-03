package com.example.trendingtimes.ui.activity

import android.content.Intent
import com.example.trendingtimes.viewmodel.NewsViewModel
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trendingtimes.SearchActivity
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.ui.adapters.ViewPagerAdapter
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ViewPagerAdapter

    @Inject
    lateinit var viewModel: NewsViewModel
    // array for slides/fragments
    private val tabTitles = arrayOf(
        "Top Headlines",
        "Technology",
        "Sports",
        "Science",
        "Health",
        "Entertainment",
        "Education",
        "Business",
        "Opinion"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.searchView.queryHint= "Search for News"

        // Recyclerview
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.navigationViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.navigationViewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        //Internet Connectivity
        if(NetworkUtils.isNetworkAvailable(this)){
            //Fetch all news
//            fetchAllNews()
        }
        else{
            Toast.makeText(this,"No Internet Connection Detected",Toast.LENGTH_SHORT).show()
        }

        binding.bookmarksButton.setOnClickListener {
            val intent = Intent(applicationContext, BookmarkedNewsActivity::class.java)
            startActivity(intent)
        }

        binding.settingsIb.setOnClickListener {
            startActivity(Intent(applicationContext,SettingsActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrEmpty()) {
                    // Perform the search operation based on the query
                    val i = Intent(applicationContext, SearchActivity::class.java)
                    i.putExtra("query",p0)
                    i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

                    startActivity(i)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })

//

//        observeNewsCategories()
    }

//    private fun fetchAllNews() {
//        viewModel.fetchBusinessNews()
//        viewModel.fetchTechnologyNews("technology")
//        viewModel.fetchEducationNews("education")
//        viewModel.fetchOpinionNews("opinion")
//        viewModel.fetchEntertainmentNews("entertainment")
//        viewModel.fetchTopHeadlinesNews("top-headlines")
//        viewModel.fetchSportsNews("sports")
//        viewModel.fetchScienceNews("science")
//        viewModel.fetchHealthNews("health")
//    }


//    private fun observeNewsCategories() {
//        //start observing changes
//        viewModel.businessNewsResponse.observe(this, Observer {
//            businessNews.addAll(it.articles)
//        })
//
//        viewModel.technologyNewsResponse.observe(this, Observer {
//            technologyNews.addAll(it.articles)
//        })
//
//        viewModel.educationNewsResponse.observe(this, Observer {
//            educationNews.addAll(it.articles)
//        })
//
//        viewModel.opinionNewsResponse.observe(this, Observer {
//            opinionNews.addAll(it.articles)
//        })
//
//        viewModel.entertainmentNewsResponse.observe(this, Observer {
//            entertainmentNews.addAll(it.articles)
//        })
//        viewModel.topHeadlinesNewsResponse.observe(this, Observer {
//            topHeadlinesNews.addAll(it.articles)
//        })
//
//        viewModel.sportsNewsResponse.observe(this, Observer {
//            sportsNews.addAll(it.articles)
//        })
//
//        viewModel.scienceNewsResponse.observe(this, Observer {
//            scienceNews.addAll(it.articles)
//        })
//
//        viewModel.healthNewsResponse.observe(this, Observer {
//            healthNews.addAll(it.articles)
//        })
//    }


    override fun onResume() {
        super.onResume()
        if(NetworkUtils.isNetworkAvailable(this)){
//            fetchAllNews()
        }
        else{
            Toast.makeText(this,"No Internet Connection Detected",Toast.LENGTH_SHORT).show()
        }

    }
}