package com.example.petproject.data.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.data.storage.relations.NoteWithTagsDb
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    fun upsertNote(noteDb: NoteDb)

    @Delete
    fun deleteNote(noteDb: NoteDb)

    @Query("SELECT * FROM note WHERE noteId=:id")
    fun getNote(id: String) : NoteDb?

    @Query("SELECT * FROM note")
    fun getNotes(): List<NoteDb>

    @Query("SELECT * FROM note")
    fun observeNotes() : Flow<List<NoteDb>>

    @Transaction
    @Query("SELECT * FROM note WHERE noteId=:id")
    fun getNoteWithTags(id: String) : NoteWithTagsDb?

    @Transaction
    @Query("SELECT * FROM note")
    fun observeNoteWithTags(): Flow<List<NoteWithTagsDb>>
}