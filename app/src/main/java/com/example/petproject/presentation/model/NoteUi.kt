package com.example.petproject.presentation.model

import java.util.Date

data class NoteUi(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val tags: List<TagUi> = emptyList(),
    val pinned: Boolean = false,
    val lastUpdate: Date = Date(0L)
)
