package com.example.petproject.domain.usecases.note

import com.example.petproject.data.storage.mappers.note.NoteDbToDomainMapper
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.mappers.NoteToDbMapper
import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface PinNotesUseCase {
    suspend fun getNotesByIds(ids: List<String>) : List<Note>?
    suspend fun pinNotes(notes: List<Note>, pin: Boolean)
}

class PinNotesUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository,
    private val noteToDbMapper: NoteToDbMapper = NoteToDbMapper(),
    private val noteDbToDomainMapper: NoteDbToDomainMapper = NoteDbToDomainMapper()
) : PinNotesUseCase {
    override suspend fun getNotesByIds(ids: List<String>): List<Note>? {
        return noteRepository.getNotesByIds(ids)?.map { noteDbToDomainMapper(it) }
    }

    override suspend fun pinNotes(notes: List<Note>, pin: Boolean) {
        val fetchedNotes = getNotesByIds(notes.map { it.id }) ?: throw Exception("Notes not found")

        if (pin) {
            noteRepository.updateNotes(fetchedNotes.map { noteToDbMapper(it.copy(pinned = true)) })
        } else {
            noteRepository.updateNotes(fetchedNotes.map { noteToDbMapper(it.copy(pinned = false)) })
        }
    }

}