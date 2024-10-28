package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface GetNoteByIdUseCase {
    suspend fun getNoteById(id: String) : Note?
}

class GetNoteByIdUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
) : GetNoteByIdUseCase {
    override suspend fun getNoteById(id: String): Note? {
        return noteRepository.getNoteById(id) ?: Note(title = "NotFound Check UC")
    }
}