package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.FragmentEntertainmentNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity

class EntertainmentNewsFragment : Fragment(R.layout.fragment_entertainment_news) {

    private lateinit var binding: FragmentEntertainmentNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEntertainmentNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        binding.entertainmentRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        if(activity != null){
            binding.entertainmentRv.adapter = NewsAdapter(requireContext(), activity.entertainmentNews,activity.viewModel)
        }
    }
}