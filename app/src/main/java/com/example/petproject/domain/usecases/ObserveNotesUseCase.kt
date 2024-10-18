package com.example.petproject.domain.usecases

import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.repository.note.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveNotesUseCase {
    fun observeNotes(): Flow<List<Note>>
}

class ObserveNotesUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
) : ObserveNotesUseCase {
    override fun observeNotes(): Flow<List<Note>> {
        return noteRepository.getNotesStream()
    }
}