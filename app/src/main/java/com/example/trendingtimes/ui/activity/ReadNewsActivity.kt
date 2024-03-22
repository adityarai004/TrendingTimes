package com.example.trendingtimes.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import com.example.trendingtimes.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadNewsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_URL = "extra_url"
    }
    private lateinit var webView : WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_news)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Read News"
        val url = intent.getStringExtra(EXTRA_URL)
        webView = findViewById(R.id.webView)
        if (url != null) {
            webView.loadUrl(url)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}