package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.repository.note.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveNotesWithTagsUseCase {
    fun observeNotesWithTags(): Flow<List<NoteWithTags>>
}

class ObserveNotesWithTagsUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
) : ObserveNotesWithTagsUseCase {
    override fun observeNotesWithTags(): Flow<List<NoteWithTags>> {
        return noteRepository.getNotesWithTagsStream()
    }
}