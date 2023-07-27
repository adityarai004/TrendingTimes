package com.example.trendingtimes.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.trendingtimes.ui.fragments.BusinessNewsFragment
import com.example.trendingtimes.ui.fragments.EducationNewsFragment
import com.example.trendingtimes.ui.fragments.EntertainmentNewsFragment
import com.example.trendingtimes.ui.fragments.HealthNewsFragment
import com.example.trendingtimes.ui.fragments.OpinionNewsFragment
import com.example.trendingtimes.ui.fragments.ScienceNewsFragment
import com.example.trendingtimes.ui.fragments.SportsNewsFragment
import com.example.trendingtimes.ui.fragments.TechnologyNewsFragment
import com.example.trendingtimes.ui.fragments.TopHeadlinesFragment

class ViewPagerAdapter(fragmentManager : FragmentManager,lifecycle:Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 9
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
                0 -> {
                    TopHeadlinesFragment()
                }
                1 -> {
                    TechnologyNewsFragment()
                }
                2 -> {
                    SportsNewsFragment()
                }
                3 -> {
                    ScienceNewsFragment()
                }
                4 -> {
                    HealthNewsFragment()
                }
                5 -> {
                    EntertainmentNewsFragment()
                }
                6 -> {
                    EducationNewsFragment()
                }

                7 -> {
                    BusinessNewsFragment()
                }

                8 -> {
                    OpinionNewsFragment()
                }
                else->{
                    Fragment()
                }
        }

    }

}