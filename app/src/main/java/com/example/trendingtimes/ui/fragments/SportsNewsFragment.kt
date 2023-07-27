package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.FragmentSportsNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity

class SportsNewsFragment : Fragment(R.layout.fragment_sports_news) {
    private lateinit var binding: FragmentSportsNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSportsNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        binding.sportsRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        if(activity != null){
            binding.sportsRv.adapter = NewsAdapter(requireContext(), activity.sportsNews,activity.viewModel)
        }
    }
}