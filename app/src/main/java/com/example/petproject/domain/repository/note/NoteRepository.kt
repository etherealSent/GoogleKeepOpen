package com.example.petproject.domain.repository.note

import android.util.Log
import com.example.petproject.data.storage.dao.NoteDao
import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.data.storage.mappers.note.NoteDbToDomainMapper
import com.example.petproject.data.storage.mappers.note.NoteWithTagsDbToDomainMapper
import com.example.petproject.data.storage.relations.NoteWithTagsDb
import com.example.petproject.di.ApplicationScope
import com.example.petproject.di.DefaultDispatcher
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.entities.note.NoteWithTags
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

interface NoteRepository {
    fun getNotesStream(): Flow<List<Note>>
    suspend fun createNote(note: NoteDb) : String
    suspend fun getNoteById(id: String) : Note?
    suspend fun getNoteWithTagsById(id: String) : NoteWithTags?
    suspend fun updateNote(note: NoteDb)
    fun getNotesWithTagsStream(): Flow<List<NoteWithTags>>
    suspend fun deleteNote(note: NoteDb)
    suspend fun getPinnedNotesSize(): Int
    suspend fun getOtherNotesSize(): Int
    suspend fun getOtherNotesFromPosition(fromPosition: Int): List<NoteDb>?
    suspend fun getPinnedNotesFromPosition(fromPosition: Int): List<NoteDb>?
    suspend fun updateNotes(notes: List<NoteDb>)
    suspend fun getNotesByIds(ids: List<String>) : List<NoteDb>?
}

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteDbToDomainMapper: NoteDbToDomainMapper,
    private val noteWithTagsDbToDomainMapper: NoteWithTagsDbToDomainMapper,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : NoteRepository {

    override fun getNotesStream(): Flow<List<Note>> {
        return noteDao.observeNotes().map { notes ->
            withContext(dispatcher) {
                notes.map(noteDbToDomainMapper)
            }
        }
    }

    override suspend fun deleteNote(note: NoteDb) {
        noteDao.deleteNote(note)
    }

    override suspend fun getPinnedNotesSize(): Int {
        return noteDao.getPinnedNotesSize()
    }

    override suspend fun getOtherNotesSize(): Int {
        return noteDao.getOtherNotesSize()
    }

    override suspend fun getOtherNotesFromPosition(fromPosition: Int): List<NoteDb>? {
        return noteDao.getOtherNotesFromPosition(fromPosition)
    }

    override suspend fun getPinnedNotesFromPosition(fromPosition: Int): List<NoteDb>? {
        return noteDao.getPinnedNotesFromPosition(fromPosition)
    }

    override suspend fun updateNotes(notes: List<NoteDb>) {
        noteDao.updateNotes(notes)
    }

    override suspend fun getNotesByIds(ids: List<String>): List<NoteDb>? {
        return noteDao.getNotesByIds(ids)
    }

    override suspend fun createNote(note: NoteDb) : String {

        val noteId = withContext(dispatcher) {
            UUID.randomUUID().toString()
        }

        noteDao.upsertNote(note.copy(noteId = noteId))

        return noteId
    }

    override suspend fun getNoteById(id: String) : Note? {
        return noteDbToDomainMapper(
            noteDao.getNote(id) ?: NoteDb(title = "Not Found CHECK REPO", content = "$id")
        )
    }

    override suspend fun getNoteWithTagsById(id: String): NoteWithTags? {
        return noteWithTagsDbToDomainMapper(
            noteDao.getNoteWithTags(id) ?: NoteWithTagsDb()
        )
    }

    override suspend fun updateNote(note: NoteDb) {
        noteDao.upsertNote(note)
        Log.d("NoteRepository", "updateNote: $note")
    }

    override fun getNotesWithTagsStream(): Flow<List<NoteWithTags>> {
        return noteDao.observeNoteWithTags().map { notes ->
            withContext(dispatcher) {
                notes.map(noteWithTagsDbToDomainMapper)
            }
        }
    }
}