package com.example.petproject.presentation.editNote.camera

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petproject.domain.usecases.note.GetNoteByIdUseCase
import com.example.petproject.domain.usecases.note.UpdateNoteUseCase
import com.example.petproject.presentation.mappers.NoteToDomainMapper
import com.example.petproject.presentation.mappers.NoteToUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val noteToUiMapper: NoteToUiMapper = NoteToUiMapper(),
    private val noteToDomainMapper: NoteToDomainMapper = NoteToDomainMapper(),
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraState())
    val uiState = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(noteTitle = title)
        }
    }
}