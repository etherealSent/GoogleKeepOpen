package com.example.petproject.presentation.editNote

import java.util.Date

data class EditNoteState(
    val noteId: String = "",
    val title: String = "",
    val content: String = "",
    val pinned: Boolean = false,
    val isLoading: Boolean = false,
    val isNoteSaved: Boolean = false,
    val userMessage: String = "",
    val showBottomSheet: Boolean = false,
    val lastUpdate: Date = Date(0L),
    val formattedDate: String = ""
)