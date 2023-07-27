package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.FragmentScienceNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity

class ScienceNewsFragment : Fragment(R.layout.fragment_science_news) {
    private lateinit var binding: FragmentScienceNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScienceNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        binding.scienceRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        if(activity != null){
            binding.scienceRv.adapter = NewsAdapter(requireContext(), activity.scienceNews,activity.viewModel)
        }
    }
}