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
import com.example.trendingtimes.databinding.FragmentOpinionNewsBinding
import com.example.trendingtimes.ui.adapters.AdapterInterface
import com.example.trendingtimes.util.Common
import com.example.trendingtimes.util.Common.Utils.gotoReadNewsActivity
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.viewmodel.AuthViewModel
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OpinionNewsFragment : Fragment(R.layout.fragment_opinion_news) {
    private lateinit var binding: FragmentOpinionNewsBinding

    @Inject
    lateinit var viewModel: NewsViewModel

    @Inject
    lateinit var firebaseViewModel: AuthViewModel

    val list = mutableListOf<Article>()
    var currentPage = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOpinionNewsBinding.bind(view)

        if (NetworkUtils.isNetworkAvailable(requireContext())) {
            binding.noInternetLottie.visibility = View.GONE
            viewModel.fetchNews("opinion",currentPage)
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
                viewModel.fetchNews("opinion", currentPage + 1)
            }

            override fun newsClicked(news: News) {
                viewModel.addNewsToHistory(news, onSuccess = {

                },
                    onError = {

                    })
                gotoReadNewsActivity(requireActivity(), news.url, requireContext())
            }
        })


        binding.opinionRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.opinionRv.adapter = adapter

        viewModel.opinionNewsResponse.observe(this.requireActivity()) {
            if (it.articles.isNotEmpty()) {
                for (article in it.articles) {
                    list.add(article)
                    adapter.notifyItemInserted(list.size)
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}