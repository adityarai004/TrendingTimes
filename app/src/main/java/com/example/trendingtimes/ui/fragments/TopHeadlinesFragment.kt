package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.FragmentTopHeadlinesBinding
import com.example.trendingtimes.ui.activity.MainActivity

class TopHeadlinesFragment : Fragment(R.layout.fragment_top_headlines) {

    private lateinit var binding: FragmentTopHeadlinesBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTopHeadlinesBinding.bind(view)
        val activity = requireActivity() as? MainActivity

        binding.progressBar.visibility =View.VISIBLE
        binding.topHeadlinesRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        if(activity != null){
            val adapter = NewsAdapter(requireContext(),activity.topHeadlinesNews,activity.viewModel)
            binding.progressBar.visibility = View.GONE
            binding.topHeadlinesRv.adapter = adapter
        }
    }
}