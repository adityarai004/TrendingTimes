package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.databinding.FragmentScienceNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity
import com.example.trendingtimes.util.NetworkUtils
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ScienceNewsFragment : Fragment(R.layout.fragment_science_news) {
    private lateinit var binding: FragmentScienceNewsBinding

    @Inject
    lateinit var viewModel: NewsViewModel

    val list = mutableListOf<Article>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScienceNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        binding.scienceRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)

        if (NetworkUtils.isNetworkAvailable(requireContext())){
            viewModel.fetchNews("science","science")
        }
        else{
            Toast.makeText(requireContext(),"Network not available", Toast.LENGTH_LONG).show()
        }

        viewModel.scienceNewsResponse.observe(this.requireActivity()){
            if (it.articles.isNotEmpty()){
                list.clear()
                list.addAll(it.articles)
                val adapter = NewsAdapter(requireContext(),list)
                binding.progressBar.visibility = View.GONE
                binding.scienceRv.adapter = adapter
            }
        }
    }
}