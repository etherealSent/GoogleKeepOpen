package com.example.petproject.domain.usecases.note

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.mappers.NoteToDbMapper
import com.example.petproject.domain.repository.note.NoteRepository
import javax.inject.Inject

interface UpdateNoteColorUseCase {
    suspend fun getNoteById(id: String) : Note?
    suspend fun updateNoteColor(note: Note, color: Int)
}

class UpdateNoteColorUseCaseImpl @Inject constructor(
    private val noteRepository: NoteRepository,
    private val noteToDbMapper: NoteToDbMapper = NoteToDbMapper()
) : UpdateNoteColorUseCase {
    override suspend fun getNoteById(id: String): Note? {
        return noteRepository.getNoteById(id)
    }

    override suspend fun updateNoteColor(note: Note, color: Int) {
        val fetchedNote = getNoteById(note.id)?.copy(
            color = color
        ) ?: throw Exception("Task (id ${note.id}) not found")
        Log.d("sdsdsdsd","$color")

        noteRepository.updateNote(noteToDbMapper(fetchedNote))

    }
}