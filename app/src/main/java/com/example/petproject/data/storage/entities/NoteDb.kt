package com.example.petproject.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.petproject.data.storage.converters.DataConverters
import com.example.petproject.data.storage.converters.PhotoPathsConverters
import com.example.petproject.data.storage.entities.NoteDb.Companion.NOTE_TABLE_NAME
import java.util.Date

@Entity(tableName = NOTE_TABLE_NAME)
data class NoteDb(
    @PrimaryKey val noteId: String = "",
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "content") val content: String = "",
    @ColumnInfo(name = "pinned") val pinned: Boolean = false,
    @ColumnInfo(name = "lastUpdate") @TypeConverters(DataConverters::class) val lastUpdate: Date = Date(0L),
    @ColumnInfo(name = "photoPaths") @TypeConverters(PhotoPathsConverters::class) val photoPaths: List<String> = emptyList(),
    @ColumnInfo(name = "isArchived") val isArchived: Boolean = false,
    @ColumnInfo(name = "isDeleted") val isDeleted: Boolean = false
) {
    companion object {
        const val NOTE_TABLE_NAME = "note"
    }
}