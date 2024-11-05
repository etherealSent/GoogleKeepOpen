package com.example.petproject.presentation.editNote

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter


fun main() {
    val a = listOf<String>()
    println(PhotoPathsConverters().fromPhotoPathsString("a;;b;;c"))
}


class PhotoPathsConverters {
    fun fromPhotoPathsString(value: String): List<String> {
        return value.split(";;").map { it.trim() }
    }

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