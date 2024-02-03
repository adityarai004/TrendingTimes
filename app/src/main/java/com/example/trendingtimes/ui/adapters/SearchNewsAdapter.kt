package com.example.trendingtimes.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trendingtimes.R
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.data.News
import com.example.trendingtimes.ui.activity.ReadNewsActivity
import com.example.trendingtimes.viewmodel.NewsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class SearchNewsAdapter(private val context : Context, private val newsList: MutableList<Article>):RecyclerView.Adapter<SearchNewsAdapter.ViewHolder>() {


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val newsImage: ImageView = itemView.findViewById(R.id.news_image)
        var newsDesc : TextView= itemView.findViewById(R.id.news_title)
        val publishTime :TextView = itemView.findViewById(R.id.news_publication_time)
        val shareBtn:ImageButton = itemView.findViewById(R.id.share_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item,parent,false))
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.apply {
            this.newsDesc.text = newsItem.title
            Glide.with(context).load(newsItem.urlToImage).into(this.newsImage)
            this.publishTime.text = getTimeDifference(newsItem.publishedAt)
            this.newsDesc.setOnLongClickListener {
//                VM.insertNews(context,newsItem)
                val db = FirebaseFirestore.getInstance()
                val currentUser = FirebaseAuth.getInstance().currentUser
                val news = newsItem.urlToImage?.let { it1 ->
                    News(
                        title = newsItem.title,
                        publishedAt = newsItem.publishedAt,
                        url = newsItem.url,
                        urlImage = it1// or reference to the image
                    )
                }

                currentUser?.uid?.let { uid ->
                    if (news != null) {
                        db.collection("users")
                            .document(uid)
                            .collection("news")
                            .add(news) // Add the news item to the "news" subcollection
                            .addOnSuccessListener {
                                Toast.makeText(context, "News item saved", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                // Handle any errors that occur
                                Log.d("TAG", "Error adding news item", e)
                                Toast.makeText(context, "Error saving news item", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                true
            }
            this.newsDesc.setOnClickListener {
                val intent = Intent(context, ReadNewsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ReadNewsActivity.EXTRA_URL, newsItem.url)
                context.startActivity(intent)
            }
            this.shareBtn.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, newsItem.url)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                context.startActivity(Intent.createChooser(shareIntent, "Share Article"))
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

    fun setList(newList: MutableList<Article>){
        newsList.clear()
        newsList.addAll(newList)
    }
}