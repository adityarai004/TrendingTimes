package com.example.trendingtimes.util

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.example.trendingtimes.ui.activity.ReadNewsActivity

class Common {
    companion object Utils {

        fun gotoReadNewsActivity(requireActivity: FragmentActivity,url: String,context: Context) {

            val intent = Intent(requireActivity, ReadNewsActivity::class.java)
            intent.putExtra(ReadNewsActivity.EXTRA_URL, url)
            context.startActivity(intent)
        }
    }
}