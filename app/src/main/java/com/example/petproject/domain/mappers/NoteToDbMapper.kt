package com.example.petproject.domain.mappers

import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.domain.entities.note.Note
import javax.inject.Inject

class NoteToDbMapper @Inject constructor() : (Note) -> NoteDb {
    override fun invoke(note: Note): NoteDb {
        return NoteDb(
            noteId = note.id,
            title = note.title,
            content = note.content,
            pinned = note.pinned,
            lastUpdate = note.lastUpdate,
            photoPaths = note.photoPaths,
            isArchived = note.isArchived
        )
    }
}