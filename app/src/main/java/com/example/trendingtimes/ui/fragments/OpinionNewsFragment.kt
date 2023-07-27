package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.FragmentOpinionNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity


class OpinionNewsFragment : Fragment(R.layout.fragment_opinion_news) {
    private lateinit var binding: FragmentOpinionNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOpinionNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        binding.opinionRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        if(activity != null){
            binding.opinionRv.adapter = NewsAdapter(requireContext(), activity.opinionNews,activity.viewModel)
        }
    }
}