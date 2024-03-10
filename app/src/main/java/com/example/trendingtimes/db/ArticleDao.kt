package com.example.trendingtimes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trendingtimes.data.News

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(news: News)

    @Delete
    suspend fun deleteArticle(news: News)

    @Query("SELECT * FROM article_table")
    fun getAllArticles(): List<News>
}