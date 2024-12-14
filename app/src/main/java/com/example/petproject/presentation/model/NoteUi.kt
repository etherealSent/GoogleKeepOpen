package com.example.petproject.presentation.model

import androidx.compose.ui.graphics.Color
import java.util.Date

data class NoteUi(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val tags: List<TagUi> = emptyList(),
    val pinned: Boolean = false,
    val lastUpdate: Date = Date(0L),
    val photoPaths: List<String> = emptyList(),
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false,
    val position: Int = 0,
    val color: Color = Color.Transparent
)
