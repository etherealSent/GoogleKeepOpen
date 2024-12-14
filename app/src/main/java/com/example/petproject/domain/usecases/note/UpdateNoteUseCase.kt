package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.mappers.NoteToDbMapper
import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface UpdateNoteUseCase {
    suspend fun getNoteById(id: String) : Note?
    suspend fun updateNote(note: Note)
}

class UpdateNoteUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository,
    private val noteToDbMapper: NoteToDbMapper = NoteToDbMapper()
) : UpdateNoteUseCase {
    override suspend fun updateNote(note: Note) {

        val fetchedNote = getNoteById(note.id)?.copy(
            title = note.title,
            content = note.content,
            pinned = note.pinned,
            lastUpdate = note.lastUpdate,
            photoPaths = note.photoPaths,
            isDeleted = note.isDeleted,
            isArchived = note.isArchived,
            position = note.position,
            color = note.color
        ) ?: throw Exception("Task (id ${note.id}) not found")

        noteRepository.updateNote(noteToDbMapper(fetchedNote))
    }

    override suspend fun getNoteById(id: String): Note? {
        return noteRepository.getNoteById(id)
    }
}