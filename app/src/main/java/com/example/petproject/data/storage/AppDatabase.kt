package com.example.petproject.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.petproject.data.storage.converters.DataConverters
import com.example.petproject.data.storage.converters.PhotoPathsConverters
import com.example.petproject.data.storage.dao.NoteDao
import com.example.petproject.data.storage.dao.TagDao
import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.data.storage.entities.TagDb
import com.example.petproject.data.storage.entities.TagNoteCrossRef

@Database(
    entities = [NoteDb::class, TagDb::class,
        TagNoteCrossRef::class],
    version = 11,
    exportSchema = false
    )
@TypeConverters(DataConverters::class, PhotoPathsConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao

    companion object {
        const val DATABASE_NAME = "notes.db"
    }
}