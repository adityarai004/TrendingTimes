package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.FragmentBusinessNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity

class BusinessNewsFragment : Fragment(R.layout.fragment_business_news) {
    private lateinit var binding: FragmentBusinessNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBusinessNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        binding.businessRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)

        if(activity != null){
            val adapter = NewsAdapter(requireContext(),activity.topHeadlinesNews,activity.viewModel)
            binding.businessRv.adapter = adapter
        }
    }
}