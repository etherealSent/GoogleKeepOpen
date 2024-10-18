package com.example.petproject.presentation.model

data class NoteUi(
    val title: String,
    val content: String,
    val tags: List<TagUi>,
    val pinned: Boolean = false
)
