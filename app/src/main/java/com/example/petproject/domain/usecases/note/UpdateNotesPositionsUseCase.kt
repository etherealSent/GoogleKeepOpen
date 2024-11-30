package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface UpdateNotesPositionsUseCase {
    suspend fun updateNotesPositions(fromPosition: Int, pinned: Boolean)
}

class UpdateNotesPositionsUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
) : UpdateNotesPositionsUseCase {
    override suspend fun updateNotesPositions(fromPosition: Int, pinned: Boolean) {
        if (pinned) {
            val notesToUpdate = noteRepository.getPinnedNotesFromPosition(fromPosition) ?: return
            noteRepository.updateNotes(notesToUpdate.map { it.copy(position = it.position - 1) })
        } else {
            val notesToUpdate = noteRepository.getOtherNotesFromPosition(fromPosition) ?: return
            noteRepository.updateNotes(notesToUpdate.map { it.copy(position = it.position - 1) })
        }
    }
}