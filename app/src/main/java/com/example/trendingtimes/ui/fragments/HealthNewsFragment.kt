package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.FragmentHealthNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity

class HealthNewsFragment : Fragment(R.layout.fragment_health_news) {
    private lateinit var binding: FragmentHealthNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHealthNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        binding.healthRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        if(activity != null){
            binding.healthRv.adapter = NewsAdapter(requireContext(), activity.healthNews,activity.viewModel)
        }
    }
}