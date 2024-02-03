package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.databinding.FragmentSportsNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity
import com.example.trendingtimes.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SportsNewsFragment : Fragment(R.layout.fragment_sports_news) {
    private lateinit var binding: FragmentSportsNewsBinding

    @Inject
    lateinit var viewModel: NewsViewModel
    val list = mutableListOf<Article>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSportsNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        viewModel.fetchNews("sports","Sports")

        binding.sportsRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        viewModel.sportsNewsResponse.observe(this.requireActivity()){
            if (it.articles.isNotEmpty()){
                list.clear()
                list.addAll(it.articles)
                val adapter = NewsAdapter(requireContext(),list)
                binding.progressBar.visibility = View.GONE
                binding.sportsRv.adapter = adapter
            }
        }
    }
}