package com.example.petproject.domain.entities.note

import com.example.petproject.domain.entities.tag.Tag

data class NoteWithTags(
    val title: String,
    val content: String,
    val pinned: Boolean,
    val tags: List<Tag>?
)