package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface GetAmountOfOtherNotesUseCase {
    suspend fun getAmountOfOtherNotes() : Int
}

class GetAmountOfOtherNotesUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
) : GetAmountOfOtherNotesUseCase {
    override suspend fun getAmountOfOtherNotes(): Int {
        return noteRepository.getOtherNotesSize()
    }
}