package com.example.petproject.domain.entities.note

import java.util.Date

data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val pinned: Boolean = false,
    val lastUpdate: Date = Date(0L),
    val photoPaths: List<String> = emptyList(),
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false,
    val position: Int = 0
)