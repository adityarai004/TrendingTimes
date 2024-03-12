package com.example.trendingtimes.db

import com.example.trendingtimes.util.SourceTypeConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trendingtimes.model.local.News

@Database(entities = [News::class], version = 1,exportSchema = false)
@TypeConverters(SourceTypeConverter::class)
abstract class ArticleDatabase: RoomDatabase() {
    abstract fun articleDao(): ArticleDao

}
