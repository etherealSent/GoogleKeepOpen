package com.example.petproject.presentation.editTags

import com.example.petproject.presentation.model.TagUi

data class EditTagsState(
    val tags: List<TagUi> = emptyList(),
    val isLoading: Boolean = false,
    val newTagName: String = "",
    val editingId: String = "",
    val mutableTags: List<TagUi> = emptyList()
)

data class EditTagsScreenState(
    val newTagName: String = "",
    val editingId: String = "",
)