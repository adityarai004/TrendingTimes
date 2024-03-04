package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.data.News
import com.example.trendingtimes.databinding.FragmentTechnologyNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity
import com.example.trendingtimes.ui.adapters.LongPress
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class TechnologyNewsFragment : Fragment(R.layout.fragment_technology_news) {
    private lateinit var binding: FragmentTechnologyNewsBinding

    @Inject
    lateinit var viewModel: NewsViewModel
    val list = mutableListOf<Article>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTechnologyNewsBinding.bind(view)
        binding.progressBar.visibility =View.VISIBLE
        if (NetworkUtils.isNetworkAvailable(requireContext())){
            viewModel.fetchNews("technology","technology")
        }
        else{
            Toast.makeText(requireContext(),"Network not available", Toast.LENGTH_LONG).show()
        }
        binding.technologyRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)

        viewModel.technologyNewsResponse.observe(this.requireActivity()){
            if (it.articles.isNotEmpty()){
                list.clear()
                list.addAll(it.articles)
                val adapter = NewsAdapter(requireContext(),list,object : LongPress {
                    override fun didLongPress(news: News) {
                        viewModel.insertNews(news)
                    }
                })
                binding.progressBar.visibility = View.GONE
                binding.technologyRv.adapter = adapter
            }
        }
    }
}