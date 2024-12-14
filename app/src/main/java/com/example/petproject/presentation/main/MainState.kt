package com.example.petproject.presentation.main

import androidx.compose.ui.graphics.Color
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi

data class MainState(
    val notesWithTags: List<NoteUi> = emptyList(),
    val isLoading: Boolean = false,
    val tags: List<TagUi> = emptyList(),
    val uiMainState: UiMainState = UiMainState()
)

data class UiMainState(
    val screenType: MainScreenType = MainScreenType.Notes,
    val selectedTagUi: TagUi = TagUi(),
    val selectedIndex: Int = -1,
    val notesViewType: NotesViewType = NotesViewType.Column,
    val noteSelected: Boolean = false,
    val notesSelected: List<NoteUi> = emptyList(),
    val menuExpanded: Boolean = false,
    val pickedColor: Color = Color.Transparent,
    val showColorDialog: Boolean = false
)

enum class NotesViewType {
    Grid, Column
}