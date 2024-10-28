package com.example.petproject.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.petproject.data.storage.entities.NoteDb.Companion.NOTE_TABLE_NAME
import java.util.Date

@Entity(tableName = NOTE_TABLE_NAME)
data class NoteDb(
    @PrimaryKey val noteId: String = "",
    @ColumnInfo(name = "title") val title: String = "",
    @ColumnInfo(name = "content") val content: String = "",
    @ColumnInfo(name = "pinned") val pinned: Boolean = false,
    @ColumnInfo(name = "lastUpdate") val lastUpdate: Date = Date(0L)
) {
    companion object {
        const val NOTE_TABLE_NAME = "note"
    }
}