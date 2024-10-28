package com.example.petproject.presentation.editNote

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.usecases.note.GetNoteByIdUseCase
import com.example.petproject.domain.usecases.note.SaveNoteUseCase
import com.example.petproject.domain.usecases.note.UpdateNoteUseCase
import com.example.petproject.presentation.mappers.NoteToDomainMapper
import com.example.petproject.presentation.model.NoteUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val noteToDomainMapper: NoteToDomainMapper = NoteToDomainMapper(),
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: String? = savedStateHandle.get<String>("noteId")

    private val _uiState = MutableStateFlow(EditNoteState())
    val uiState = _uiState.asStateFlow()

    init {
        if (noteId != null) {
            loadNote(noteId)
        }
        Log.d("NoteDetailsViewModel", "NoteID: $noteId")
    }

    fun saveNote() {
        if (_uiState.value.title.isEmpty() && _uiState.value.content.isEmpty()) {
            _uiState.update {
                it.copy(
                    userMessage = "Заметка пуста"
                )
            }
            return
        }

        if (noteId == null) {
            createNewNote()
        } else {
            updateNote()
        }
    }

    private fun createNewNote() {
        viewModelScope.launch {
            saveNoteUseCase.saveNote(
                noteToDomainMapper(
                    _uiState.value.run {
                        NoteUi(id = "", title, content, listOf(), pinned, lastUpdate)
                    }
                )
            )
        }
    }

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun updateContent(content: String) {
        _uiState.update {
            it.copy(content = content)
        }
    }

    fun updatePinned() {
        _uiState.update {
            it.copy(pinned = !_uiState.value.pinned)
        }
    }

    fun updateShowBottomSheet(bool: Boolean) {
        _uiState.update {
            it.copy(showBottomSheet = bool)
        }
    }

    private fun updateNote() {
        if (noteId == null) {
            throw RuntimeException("updateNote() was called but note is new.")
        }
        viewModelScope.launch {
            updateNoteUseCase.updateNote(
                uiState.value.run {
                    Note(
                        id = noteId,
                        title = title,
                        content = content,
                        pinned = pinned,
                        lastUpdate = lastUpdate
                    )
                }
            )
        }
    }

    fun updateLastUpdateTime(date: Date) {
        _uiState.update {
            it.copy(
                lastUpdate = date
            )
        }
    }

    fun formatLastUpdateTime(currentTime: Date) {

        val timeDifference = Date(currentTime.time - uiState.value.lastUpdate.time)
        val formatter = SimpleDateFormat("HH:mm:ss")
        val formattedTime = formatter.format(timeDifference)

        _uiState.update {
            it.copy(
                formattedDate = formattedTime
            )
        }
    }

    private fun loadNote(noteId: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            getNoteByIdUseCase.getNoteById(noteId).let { note ->
                if (note != null) {
                    _uiState.update {
                        it.copy(
                            noteId = note.id,
                            title = note.title,
                            content = note.content,
                            pinned = note.pinned,
                            isLoading = false,
                            lastUpdate = note.lastUpdate
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }


}