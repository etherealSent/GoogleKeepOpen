package com.example.petproject.presentation.mappers

import com.example.petproject.domain.entities.note.Note
import com.example.petproject.presentation.model.NoteUi
import javax.inject.Inject

class NoteToUiMapper @Inject constructor() : (Note) -> NoteUi {
    override fun invoke(note: Note): NoteUi {
        return NoteUi(
                id = note.id,
                title = note.title,
                content = note.content,
                pinned = note.pinned,
                tags = listOf(),
                lastUpdate = note.lastUpdate
        )
    }
}