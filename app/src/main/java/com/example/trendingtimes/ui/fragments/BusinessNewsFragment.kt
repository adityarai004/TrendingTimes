package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.model.remote.Article
import com.example.trendingtimes.model.local.News
import com.example.trendingtimes.databinding.FragmentBusinessNewsBinding
import com.example.trendingtimes.ui.adapters.AdapterInterface
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BusinessNewsFragment : Fragment(R.layout.fragment_business_news) {
    private lateinit var binding: FragmentBusinessNewsBinding

    @Inject
    lateinit var viewModel: NewsViewModel

    val list = mutableListOf<Article>()
    var currentPage = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBusinessNewsBinding.bind(view)

        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            binding.noInternetLottie.visibility = View.GONE
            viewModel.fetchNews( "business",currentPage)
        } else {
            binding.progressBar.visibility = View.GONE
        }

        val adapter = NewsAdapter(requireContext(), list, object : AdapterInterface {
            override fun didLongPress(news: News) {
                viewModel.insertNews(news,
                    onSuccess = {
                        Toast.makeText(
                            requireContext(),
                            "News bookmarked successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onError = {
                        Toast.makeText(
                            requireContext(),
                            "Unable to bookmark news at the moment.",
                            Toast.LENGTH_LONG
                        ).show()
                    })
            }

            override fun endOfList() {
                currentPage++
                viewModel.fetchNews("business",currentPage)
            }
        })

        //Recycler view
        binding.businessRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

        binding.businessRv.adapter = adapter

        viewModel.businessNewsResponse.observe(this.requireActivity()) {
            if (it.articles.isNotEmpty()) {
                for (article in it.articles){
                    list.add(article)
                    adapter.notifyItemInserted(list.size)
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}