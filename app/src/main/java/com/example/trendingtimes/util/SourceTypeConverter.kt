package com.example.trendingtimes.util

import androidx.room.TypeConverter
import com.example.trendingtimes.data.Source
import com.google.gson.Gson

class SourceTypeConverter {
    @TypeConverter
    fun fromSource(source: Source): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(sourceString: String): Source {
        return Gson().fromJson(sourceString, Source::class.java)
    }
}