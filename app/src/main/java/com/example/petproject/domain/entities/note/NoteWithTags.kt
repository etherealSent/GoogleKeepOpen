package com.example.petproject.domain.entities.note

import com.example.petproject.domain.entities.tag.Tag

data class NoteWithTags(
    val note: Note = Note(),
    val tags: List<Tag> = emptyList()
)