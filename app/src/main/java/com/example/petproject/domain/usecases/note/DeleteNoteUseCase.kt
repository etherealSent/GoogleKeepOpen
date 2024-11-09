package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.mappers.NoteToDbMapper
import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface DeleteNoteUseCase {
    suspend fun deleteNote(note: Note)
}

class DeleteNoteUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository,
    private val noteToDbMapper: NoteToDbMapper = NoteToDbMapper()
) : DeleteNoteUseCase {
    override suspend fun deleteNote(note: Note) {
        noteRepository.deleteNote(noteToDbMapper(note))
    }
}