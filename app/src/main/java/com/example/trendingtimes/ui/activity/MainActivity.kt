package com.example.trendingtimes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.example.trendingtimes.databinding.ActivityMainBinding
import com.example.trendingtimes.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ViewPagerAdapter

    // array for slides/fragments
    private val tabTitles = arrayOf(
        "Top Headlines",
        "Technology",
        "Sports",
        "Science",
        "Health",
        "Entertainment",
        "Education",
        "Business",
        "Opinion"
    )

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GlobalScope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                println("FCM Token: $token")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if(intent != null && intent.hasExtra("news")) {
            val readNews = Intent(this,ReadNewsActivity::class.java)
            readNews.putExtra("extra_url",intent.getStringExtra("news"))
            startActivity(readNews)
        }
        initialBindings()


        val currUser = FirebaseAuth.getInstance().currentUser
        currUser?.let {
            FirebaseFirestore.getInstance().collection("users")
                .document(currUser.uid)
                .get()
                .addOnSuccessListener { docSnapshot ->
                    if (docSnapshot != null) {
                        val imageUrl = docSnapshot.get("imageUrl")
                        Glide.with(this@MainActivity).load(
                            imageUrl
                        ).into(binding.accountIb)
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Exception is $it")
                }
        }
    }

    private fun initialBindings(){

        TabLayoutMediator(binding.tabLayout, binding.navigationViewPager) { tab, position ->
            binding.searchView.clearFocus()
            tab.text = tabTitles[position]
        }.attach()

        binding.searchView.queryHint = "Search for News"
        binding.searchView.setIconifiedByDefault(false)
        binding.bookmarksButton.setOnClickListener {
            val intent = Intent(applicationContext, BookmarkedNewsActivity::class.java)
            startActivity(intent)
        }

        binding.settingsIb.setOnClickListener {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrEmpty()) {
                    // Perform the search operation based on the query
                    val i = Intent(applicationContext, SearchActivity::class.java)
                    i.putExtra("query", p0)
                    i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

                    startActivity(i)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })

        binding.accountIb.setOnClickListener {
            startActivity(Intent(this, UpdateProfileActivity::class.java))
        }
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.navigationViewPager.adapter = adapter
    }
}