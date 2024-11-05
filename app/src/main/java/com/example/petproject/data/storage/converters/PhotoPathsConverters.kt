package com.example.petproject.data.storage.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.Date

@ProvidedTypeConverter
class PhotoPathsConverters {
    @TypeConverter
    fun fromPhotoPathsString(value: String): List<String> {
        return value.split(";;").map { it.trim() }
    }

    @TypeConverter
    fun listPhotoPathsToPhotoPathsString(value: List<String>): String {
        val result = StringBuilder()
        for (i in value.indices) {
            result.append(value[i])
            if (i < value.size - 1) {
                result.append(";;")
            }}
        return result.toString()
    }
}