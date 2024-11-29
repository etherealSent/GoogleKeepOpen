package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface GetAmountOfPinnedNotesUseCase {
    suspend fun getAmountOfPinnedNotes() : Int
}

class GetAmountOfPinnedNotesUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
) : GetAmountOfPinnedNotesUseCase {
    override suspend fun getAmountOfPinnedNotes(): Int {
        return noteRepository.getPinnedNotesSize()
    }
}