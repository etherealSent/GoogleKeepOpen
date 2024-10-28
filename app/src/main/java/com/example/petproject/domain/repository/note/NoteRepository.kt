package com.example.petproject.domain.repository.note

import com.example.petproject.data.storage.dao.NoteDao
import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.data.storage.mappers.note.NoteDbToDomainMapper
import com.example.petproject.data.storage.mappers.note.NotesDbToDomainMapper
import com.example.petproject.di.ApplicationScope
import com.example.petproject.di.DefaultDispatcher
import com.example.petproject.domain.entities.note.Note
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
    suspend fun createNote(note: NoteDb)
    suspend fun getNoteById(id: String) : Note?
    suspend fun updateNote(note: NoteDb)
}

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val notesDbToDomainMapper: NotesDbToDomainMapper,
    private val noteDbToDomainMapper: NoteDbToDomainMapper,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : NoteRepository {

    override fun getNotesStream(): Flow<List<Note>> {
        return noteDao.observeNotes().map { notes ->
            withContext(dispatcher) {
                notesDbToDomainMapper(notes)
            }
        }
    }

    override suspend fun createNote(note: NoteDb) {

        val noteId = withContext(dispatcher) {
            UUID.randomUUID().toString()
        }

        noteDao.upsertNote(note.copy(noteId = noteId))
    }

    override suspend fun getNoteById(id: String) : Note? {
        return noteDbToDomainMapper(
            noteDao.getNote(id) ?: NoteDb(title = "Not Found CHECK REPO", content = "$id")
        )
    }

    override suspend fun updateNote(note: NoteDb) {
        noteDao.upsertNote(note)
    }
}