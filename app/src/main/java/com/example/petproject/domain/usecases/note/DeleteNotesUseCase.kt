package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface DeleteNotesUseCase {
    suspend fun deleteNotes(noteIds: List<String>)
}

class DeleteNotesUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
): DeleteNotesUseCase {
    override suspend fun deleteNotes(noteIds: List<String>) {
        val fetchedNotes = noteRepository.getNotesByIds(noteIds)
        fetchedNotes?.let {
            noteRepository.updateNotes(it.map { it.copy(isDeleted = true) })
        }
    }
}