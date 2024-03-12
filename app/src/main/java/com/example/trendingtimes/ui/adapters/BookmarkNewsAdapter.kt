package com.example.trendingtimes.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trendingtimes.R
import com.example.trendingtimes.model.local.News
import com.example.trendingtimes.ui.activity.ReadNewsActivity

class BookmarkNewsAdapter(private val context: Context,val newsList: MutableList<News>):RecyclerView.Adapter<BookmarkNewsAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val newsImage: ImageView = itemView.findViewById(R.id.news_image)
        var newsDesc: TextView = itemView.findViewById(R.id.news_title)
        val publishTime: TextView = itemView.findViewById(R.id.news_publication_time)
        val shareBtn: ImageButton = itemView.findViewById(R.id.share_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false))
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.apply {
            this.newsDesc.text = newsItem.title
            Glide.with(context).load(newsItem.urlImage).into(this.newsImage)
            this.publishTime.text = newsItem.publishedAt
            this.newsDesc.setOnClickListener {
                val intent = Intent(context, ReadNewsActivity::class.java)
                intent.putExtra(ReadNewsActivity.EXTRA_URL, newsItem.url)
                context.startActivity(intent)
            }
            this.shareBtn.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, newsItem.url)
                if (shareIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share Article"))
                }
            }
        }
    }

//    private fun getTimeDifference(timestamp: String): String {
//        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
//        sdf.timeZone = TimeZone.getTimeZone("UTC")
//
//        val date = sdf.parse(timestamp)
//        val currentTime = System.currentTimeMillis()
//
//        val diffMillis = currentTime - date!!.time
//        val diffSeconds = diffMillis / 1000
//        val diffMinutes = diffSeconds / 60
//        val diffHours = diffMinutes / 60
//        val diffDays = diffHours / 24
//
//        return when {
//            diffDays > 0 -> "$diffDays day${if (diffDays > 1) "s" else ""} ago"
//            diffHours > 0 -> "$diffHours hour${if (diffHours > 1) "s" else ""} ago"
//            diffMinutes > 0 -> "$diffMinutes minute${if (diffMinutes > 1) "s" else ""} ago"
//            else -> "Just now"
//        }
//    }
}
