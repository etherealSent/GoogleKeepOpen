package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.mappers.NoteToDbMapper
import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface SaveNoteUseCase {
    suspend fun saveNote(note: Note)
}

class SaveNoteUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository,
    private val noteToDbMapper: NoteToDbMapper = NoteToDbMapper()
) : SaveNoteUseCase {
    override suspend fun saveNote(note: Note) {
        return noteRepository.createNote(noteToDbMapper(note))
    }
}