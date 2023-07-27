package com.example.trendingtimes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendingtimes.ui.adapters.NewsAdapter
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.FragmentEducationNewsBinding
import com.example.trendingtimes.ui.activity.MainActivity

class EducationNewsFragment : Fragment(R.layout.fragment_education_news) {
    private lateinit var binding: FragmentEducationNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEducationNewsBinding.bind(view)
        val activity = requireActivity() as? MainActivity
        binding.educationRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        if(activity != null){
            binding.educationRv.adapter = NewsAdapter(requireContext(), activity.educationNews,activity.viewModel)
        }
    }
}