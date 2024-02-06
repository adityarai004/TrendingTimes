package com.example.trendingtimes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.annotation.Nullable


@Entity(tableName = "article_table")
data class Article(
    val author: String? = null,
    val content: String,
    val description: String,
    var publishedAt: String,

    val source: Source,
    val title: String,
    @PrimaryKey
    val url: String,
    val urlToImage: String? = null
){
    init {
        publishedAt = getTimeDifference(publishedAt)
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