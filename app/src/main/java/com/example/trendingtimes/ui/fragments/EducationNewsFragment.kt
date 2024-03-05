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
import com.example.trendingtimes.databinding.FragmentEducationNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity
import com.example.trendingtimes.ui.adapters.LongPress
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class EducationNewsFragment : Fragment(R.layout.fragment_education_news) {
    private lateinit var binding: FragmentEducationNewsBinding
    @Inject
    lateinit var viewModel: NewsViewModel
    val list = mutableListOf<Article>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEducationNewsBinding.bind(view)

        if (NetworkUtils.isNetworkAvailable(requireContext())){
            viewModel.fetchNews("education","education")
        }
        else{
            Toast.makeText(requireContext(),"Network not available", Toast.LENGTH_LONG).show()
        }
        binding.educationRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        viewModel.educationNewsResponse.observe(this.requireActivity()){
            if (it.articles.isNotEmpty()){
                list.clear()
                list.addAll(it.articles)
                val adapter = NewsAdapter(requireContext(),list,object : LongPress {
                    override fun didLongPress(news: News) {
                        viewModel.insertNews(news,
                            onSuccess = {
                                Toast.makeText(requireContext(),"News bookmarked successfully",Toast.LENGTH_LONG).show()
                            },
                            onError = {
                                Toast.makeText(requireContext(),"Unable to bookmark news at the moment.",Toast.LENGTH_LONG).show()
                            })
                    }
                })
                binding.progressBar.visibility = View.GONE
                binding.educationRv.adapter = adapter
            }
        }
    }
}