package com.example.petproject.domain.entities.tag

import com.example.petproject.domain.entities.note.Note

data class TagWithNotes(
    val tag: Tag,
    val notes: List<Note>
)