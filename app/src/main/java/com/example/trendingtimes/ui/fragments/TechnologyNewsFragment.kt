package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.FragmentTechnologyNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity

class TechnologyNewsFragment : Fragment(R.layout.fragment_technology_news) {
    private lateinit var binding: FragmentTechnologyNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTechnologyNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        binding.progressBar.visibility =View.VISIBLE

        binding.technologyRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        if(activity != null){
            binding.technologyRv.adapter = NewsAdapter(requireContext(), activity.technologyNews,activity.viewModel)
        }
    }
}