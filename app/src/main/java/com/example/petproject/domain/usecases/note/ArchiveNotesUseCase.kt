package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface ArchiveNotesUseCase {
    suspend fun archiveNotes(noteIds: List<String>)
}

class ArchiveNotesUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
): ArchiveNotesUseCase {
    override suspend fun archiveNotes(noteIds: List<String>) {
        val fetchedNotes = noteRepository.getNotesByIds(noteIds)
        fetchedNotes?.let {
            noteRepository.updateNotes(it.map { it.copy(isArchived = true) })
        }
    }
}