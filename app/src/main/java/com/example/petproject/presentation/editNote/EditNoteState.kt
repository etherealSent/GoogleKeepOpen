package com.example.petproject.presentation.editNote

import java.util.Date

data class EditNoteState(
    val noteId: String = "",
    val title: String = "",
    val content: String = "",
    val pinned: Boolean = false,
    val photoPaths: List<String> = listOf(),
    val isLoading: Boolean = false,
    val isNoteSaved: Boolean = false,
    val userMessage: String = "",
    val showBottomSheet: Boolean = false,
    val lastUpdate: Date = Date(0L),
    val formattedDate: String = "",
    val isCamera: Boolean = false,
    val bottomSheetType: BottomSheetType = BottomSheetType.Add,
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false,
    val copiedId: String = ""
)