package com.example.petproject.presentation.mappers

import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import javax.inject.Inject

class NoteWithTagsToUiMapper @Inject constructor() : (NoteWithTags) -> NoteUi {
    override fun invoke(note: NoteWithTags): NoteUi {
        return NoteUi(
            id = note.note.id,
            title = note.note.title,
            content = note.note.content,
            pinned = note.note.pinned,
            tags = note.tags.map {
                TagUi(
                    id = it.id,
                    name = it.name
                )
            },
            lastUpdate = note.note.lastUpdate,
            photoPaths = note.note.photoPaths,
            isArchived = note.note.isArchived,
            isDeleted = note.note.isDeleted
        )
    }
}