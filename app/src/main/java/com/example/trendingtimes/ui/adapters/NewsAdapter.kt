package com.example.trendingtimes.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trendingtimes.R
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.data.News
import com.example.trendingtimes.databinding.NewsItemBinding
import com.example.trendingtimes.ui.activity.ReadNewsActivity
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter(private val context : Context, private val newsList: List<Article>,private val adapterInterface: AdapterInterface):RecyclerView.Adapter<NewsAdapter.ViewHolder>() {


    class ViewHolder(private val binding: NewsItemBinding):RecyclerView.ViewHolder(binding.root){
        val mBinding : NewsItemBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NewsItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.mBinding.item = newsItem;
        if (position == itemCount - 1){
            adapterInterface.endOfList()
        }
        holder.mBinding.apply {
            Glide.with(context).load(newsItem.urlToImage).placeholder(R.drawable.img_1).into(newsImage)
            newsPublicationTime.text = getTimeDifference(newsItem.publishedAt)
            newsTitle.setOnLongClickListener {

                val news = News(
                        title = newsItem.title,
                        publishedAt = newsItem.publishedAt,
                        url = newsItem.url,
                        urlImage = newsItem.urlToImage ?: "")// or reference to the image)

                adapterInterface.didLongPress(news)
                true
            }
            newsTitle.setOnClickListener {
                val intent = Intent(context, ReadNewsActivity::class.java)
                intent.putExtra(ReadNewsActivity.EXTRA_URL, newsItem.url)
                context.startActivity(intent)
            }
            shareButton.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, newsItem.url)
                if (shareIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share Article"))
                }
            }
        }
    }

    private fun getTimeDifference(timestamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val date = sdf.parse(timestamp)
        val currentTime = System.currentTimeMillis()

        val diffMillis = currentTime - date!!.time
        val diffSeconds = diffMillis / 1000
        val diffMinutes = diffSeconds / 60
        val diffHours = diffMinutes / 60
        val diffDays = diffHours / 24

        return when {
            diffDays > 0 -> "$diffDays day${if (diffDays > 1) "s" else ""} ago"
            diffHours > 0 -> "$diffHours hour${if (diffHours > 1) "s" else ""} ago"
            diffMinutes > 0 -> "$diffMinutes minute${if (diffMinutes > 1) "s" else ""} ago"
            else -> "Just now"
        }
    }
}

interface AdapterInterface{
    fun didLongPress(news: News)
    fun endOfList()
}