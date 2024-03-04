package com.example.trendingtimes.db

import com.example.trendingtimes.util.SourceTypeConverter
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trendingtimes.data.Article
import com.example.trendingtimes.data.News

@Database(entities = [News::class], version = 1,exportSchema = false)
@TypeConverters(SourceTypeConverter::class)
abstract class ArticleDatabase: RoomDatabase() {
    abstract fun articleDao(): ArticleDao

}
