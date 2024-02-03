package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.databinding.FragmentBusinessNewsBinding
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class BusinessNewsFragment : Fragment(R.layout.fragment_business_news) {
    private lateinit var binding: FragmentBusinessNewsBinding
    @Inject
    lateinit var viewModel: NewsViewModel

    val list = mutableListOf<Article>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBusinessNewsBinding.bind(view)

        viewModel.fetchNews("business","business")

        //Recycler view
        binding.businessRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)

        viewModel.businessNewsResponse.observe(this.requireActivity()){
            if (it.articles.isNotEmpty()){
                list.clear()
                list.addAll(it.articles)
                val adapter = NewsAdapter(requireContext(),list)
                binding.progressBar.visibility = View.GONE
                binding.businessRv.adapter = adapter
            }
        }
    }
}