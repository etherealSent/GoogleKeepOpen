package com.example.petproject.data.storage.mappers.note

import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.domain.entities.note.Note
import javax.inject.Inject

class NotesDbToDomainMapper @Inject constructor() : (List<NoteDb>) -> List<Note> {
    override fun invoke(notesDb: List<NoteDb>): List<Note> {
        return notesDb.map { noteDb ->
            Note(
                id = noteDb.noteId,
                title = noteDb.title,
                content = noteDb.content,
                pinned = noteDb.pinned,
                lastUpdate = noteDb.lastUpdate,
                photoPaths = noteDb.photoPaths,
                isArchived = noteDb.isArchived
            )
        }
    }
}