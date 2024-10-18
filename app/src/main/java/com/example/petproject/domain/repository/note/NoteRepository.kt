package com.example.petproject.domain.repository.note

import com.example.petproject.data.storage.dao.NoteDao
import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.data.storage.mappers.note.NotesDbToDomainMapper
import com.example.petproject.di.ApplicationScope
import com.example.petproject.di.DefaultDispatcher
import com.example.petproject.domain.entities.note.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NoteRepository {
    suspend fun getNote(): List<Note>
    fun getNotesStream(): Flow<List<Note>>
    suspend fun createNote(note: NoteDb)
}

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val notesDbToDomainMapper: NotesDbToDomainMapper,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : NoteRepository {
    override suspend fun getNote(): List<Note> {
        return listOf()
    }


    override fun getNotesStream(): Flow<List<Note>> {
        return noteDao.observeNotes().map { notes ->
            withContext(dispatcher) {
                notesDbToDomainMapper.invoke(notes)
            }
        }
    }

    override suspend fun createNote(note: NoteDb) {
        noteDao.insertNote(note)
    }
}