package com.example.petproject.domain.usecases.note

import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface GetNoteWithTagsByIdUseCase {
    suspend fun getNoteById(id: String) : NoteWithTags?
}

class GetNoteWithTagsByIdUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository
) : GetNoteWithTagsByIdUseCase {
    override suspend fun getNoteById(id: String): NoteWithTags? {
        return noteRepository.getNoteWithTagsById(id) ?: NoteWithTags()
    }
}