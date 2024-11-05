package com.example.petproject.utils.files

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class FileHelper(private val context: Context) {
    fun getUriFromFile(file: File): Uri {
        return FileProvider.getUriForFile(context, "com.example.petproject", file)
    }
    fun getPicturesFolder(): String =
        Environment.DIRECTORY_PICTURES
}