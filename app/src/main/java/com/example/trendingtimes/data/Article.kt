package com.example.trendingtimes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nullable


@Entity(tableName = "article_table")
data class Article(
    val author: String? = null,
    val content: String,
    val description: String,
    val publishedAt: String,

    val source: Source,
    val title: String,
    @PrimaryKey
    val url: String,
    val urlToImage: String? = null
)