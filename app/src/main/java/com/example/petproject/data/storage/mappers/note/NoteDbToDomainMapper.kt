package com.example.petproject.data.storage.mappers.note

import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.domain.entities.note.Note
import javax.inject.Inject

class NoteDbToDomainMapper @Inject constructor() : (NoteDb) -> Note {
    override fun invoke(noteDb: NoteDb): Note {
        return Note(
            id = noteDb.noteId,
            title = noteDb.title,
            content = noteDb.content,
            pinned = noteDb.pinned,
            lastUpdate = noteDb.lastUpdate
        )
    }
}