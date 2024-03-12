package com.example.trendingtimes.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_table")
data class News(
    var id: String? = "",
    val title: String,
    var publishedAt: String,
    @PrimaryKey
    val url: String,
    val urlImage: String
)
