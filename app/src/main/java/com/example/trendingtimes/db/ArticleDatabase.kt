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

@Database(entities = [Article::class], version = 3,exportSchema = false)
@TypeConverters(SourceTypeConverter::class)
abstract class ArticleDatabase: RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object{
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE article_table_new (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "author TEXT, " +
                            "content TEXT NOT NULL, " +
                            "description TEXT NOT NULL, " +
                            "publishedAt TEXT NOT NULL, " +
                            "source TEXT NOT NULL, " +
                            "title TEXT NOT NULL, " +
                            "url TEXT NOT NULL, " +
                            "urlToImage TEXT" +
                            ")"
                )
                database.execSQL("INSERT INTO article_table_new (author, content, description, publishedAt, source, title, url, urlToImage) SELECT author, content, description, publishedAt, source, title, url, urlToImage FROM article_table")

                database.execSQL("DROP TABLE article_table")

                database.execSQL("ALTER TABLE article_table_new RENAME TO article_table")
            }
        }
        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        fun getDatabase(context: Context): ArticleDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    ArticleDatabase::class.java,
                    "article_table")
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}
