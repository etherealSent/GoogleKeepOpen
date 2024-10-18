package com.example.petproject.domain.entities.note

data class Note(
    val title: String,
    val content: String,
    val pinned: Boolean
)
