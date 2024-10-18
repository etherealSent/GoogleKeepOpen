package com.example.petproject.presentation.main

import com.example.petproject.presentation.model.NoteUi

data class MainState(
    val notesWithTags: List<NoteUi> = emptyList(),
    val isLoading: Boolean = false
)