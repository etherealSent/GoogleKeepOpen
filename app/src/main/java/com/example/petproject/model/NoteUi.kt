package com.example.petproject.model

data class NoteUi(
    val id: Int,
    val name: String,
    val content: String,
    val tags: List<TagUi>
)
