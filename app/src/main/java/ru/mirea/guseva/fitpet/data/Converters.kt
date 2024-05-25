package ru.mirea.guseva.fitpet.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}
