package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.databinding.FragmentOpinionNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OpinionNewsFragment : Fragment(R.layout.fragment_opinion_news) {
    private lateinit var binding: FragmentOpinionNewsBinding

    @Inject
    lateinit var viewModel: NewsViewModel

    val list = mutableListOf<Article>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOpinionNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity

        if (NetworkUtils.isNetworkAvailable(requireContext())){
            viewModel.fetchNews("opinion","Opinion")
        }
        else{
            Toast.makeText(requireContext(),"Network not available", Toast.LENGTH_LONG).show()
        }
        binding.opinionRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        viewModel.opinionNewsResponse.observe(this.requireActivity()){
            if (it.articles.isNotEmpty()){
                list.clear()
                list.addAll(it.articles)
                val adapter = NewsAdapter(requireContext(),list)
                binding.progressBar.visibility = View.GONE
                binding.opinionRv.adapter = adapter
            }
        }
    }
}