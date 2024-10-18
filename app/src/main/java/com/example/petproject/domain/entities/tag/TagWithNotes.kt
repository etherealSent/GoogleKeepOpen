package com.example.petproject.domain.entities.tag

import com.example.petproject.domain.entities.note.NotesWithTags

data class TagWithNotes(
    val tag: Tag,
    val notes: NotesWithTags
)