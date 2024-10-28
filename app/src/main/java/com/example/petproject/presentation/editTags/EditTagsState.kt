package com.example.petproject.presentation.editTags

import com.example.petproject.presentation.model.TagUi

data class EditTagsState(
    val tags: List<TagUi> = emptyList(),
    val isLoading: Boolean = false,
    val editingId: String = "",
)