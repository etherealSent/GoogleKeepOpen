package com.example.petproject.presentation.editNote

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.usecases.note.GetAmountOfOtherNotesUseCase
import com.example.petproject.domain.usecases.note.GetAmountOfPinnedNotesUseCase
import com.example.petproject.domain.usecases.note.GetNoteByIdUseCase
import com.example.petproject.domain.usecases.note.SaveNoteUseCase
import com.example.petproject.domain.usecases.note.UpdateNoteUseCase
import com.example.petproject.domain.usecases.note.UpdateNotesPositionsUseCase
import com.example.petproject.presentation.mappers.NoteToDomainMapper
import com.example.petproject.presentation.model.NoteUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getAmountOfPinnedNotesUseCase: GetAmountOfPinnedNotesUseCase,
    private val getAmountOfOtherNotesUseCase: GetAmountOfOtherNotesUseCase,
    private val updateNotesPositionsUseCase: UpdateNotesPositionsUseCase,
    private val noteToDomainMapper: NoteToDomainMapper = NoteToDomainMapper(),
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        if (noteId != null) {
            loadNote(noteId)
        }
    }

    private val noteId: String? = savedStateHandle.get<String>("noteId")

    private val _uiState = MutableStateFlow(EditNoteState())
    val uiState = _uiState.asStateFlow()

    init {
        if (noteId != null) {
            loadNote(noteId)
        }
        Log.d("NoteDetailsViewModel", "NoteID: $noteId")
    }

    private lateinit var initState: EditNoteState

    fun saveNote(date: Date) {
        if (_uiState.value.title.isEmpty() && _uiState.value.content.isEmpty()) {
            _uiState.update {
                it.copy(
                    userMessage = "Заметка пуста"
                )
            }
            return
        }

        if (noteId == null) {
            updateLastUpdateTime(date)
            createNewNote()
        } else {
            if (initState != _uiState.value) {
                updateLastUpdateTime(date)
                updateNote()
            }
        }
    }

    private fun createNewNote() {
        viewModelScope.launch {

            val gotInListPosition = if (_uiState.value.pinned) {
                getAmountOfPinnedNotesUseCase.getAmountOfPinnedNotes()
            } else getAmountOfOtherNotesUseCase.getAmountOfOtherNotes()

           saveNoteUseCase.saveNote(
                noteToDomainMapper(
                    _uiState.value.run {
                        NoteUi(
                            id = "",
                            title,
                            content,
                            listOf(),
                            pinned,
                            lastUpdate,
                            photoPaths,
                            isArchived,
                            isDeleted,
                            gotInListPosition + 1
                        )
                    }
                )
            )

        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            val currentPosition = _uiState.value.position
            updateNotesPositionsUseCase.decNotesPositions(
                fromPosition = currentPosition + 1,
                pinned = _uiState.value.pinned
            )
        }
        _uiState.update {
            it.copy(isDeleted = true, position = 0)
        }
    }

    fun archiveNote() {

        viewModelScope.launch {
            val currentPosition = _uiState.value.position
            updateNotesPositionsUseCase.decNotesPositions(
                fromPosition = currentPosition + 1,
                pinned = _uiState.value.pinned
            )
        }

        _uiState.update {
            it.copy(isArchived = true)
        }
    }

    fun copyNote() {
        viewModelScope.launch {

            updateNotesPositionsUseCase.incNotesPositions(
                fromPosition = _uiState.value.position + 1,
                pinned = initState.pinned
            )

            val id = saveNoteUseCase.saveNote(
                noteToDomainMapper(
                    _uiState.value.run {
                        NoteUi(id = "", title, content, listOf(), pinned, lastUpdate, photoPaths, isArchived, isDeleted, position + 1)
                    }
                )
            )

            _uiState.update {
                it.copy(copiedId = id, showBottomSheet = false)
            }
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

    fun addNewPhotoPath(uri: String) {
        Log.d("NoteDetailsViewModel", "INITIAL: ${uiState.value.photoPaths}")
        val photoPaths = uiState.value.photoPaths.toMutableList()
        photoPaths.add(uri)

        _uiState.update {
            it.copy(
                photoPaths = photoPaths.toList()
            )
        }
    }

    fun updatePinned() {
        _uiState.update {
            it.copy(pinned = !_uiState.value.pinned)
        }
    }

    fun updateShowBottomSheet(bool: Boolean, type: BottomSheetType = BottomSheetType.Add) {
        _uiState.update {
            it.copy(showBottomSheet = bool, bottomSheetType = type)
        }
    }

    private fun updateNote() {
        if (noteId == null) {
            throw RuntimeException("updateNote() was called but note is new.")
        }

            viewModelScope.launch {
                val listChanged = initState.pinned != _uiState.value.pinned

                // list changed
                if (listChanged) {
                    val currentPosition = _uiState.value.position

                    val newPosition = if (_uiState.value.pinned) {
                        getAmountOfPinnedNotesUseCase.getAmountOfPinnedNotes() + 1
                    } else { getAmountOfOtherNotesUseCase.getAmountOfOtherNotes() + 1 }

                    updateNotesPositionsUseCase.decNotesPositions(
                        fromPosition = currentPosition + 1,
                        pinned = initState.pinned
                    )

                    updateNoteUseCase.updateNote(
                        uiState.value.run {
                            Note(
                                id = noteId,
                                title = title,
                                content = content,
                                pinned = pinned,
                                lastUpdate = lastUpdate,
                                photoPaths = photoPaths,
                                isArchived = isArchived,
                                isDeleted = isDeleted,
                                position = newPosition
                            )
                        }
                    )
                } else {
                    // list not changed => position not changed
                    updateNoteUseCase.updateNote(
                        uiState.value.run {
                            Note(
                                id = noteId,
                                title = title,
                                content = content,
                                pinned = pinned,
                                lastUpdate = lastUpdate,
                                photoPaths = photoPaths,
                                isArchived = isArchived,
                                isDeleted = isDeleted,
                                position = initState.position
                            )
                        }
                    )
                }
            }
    }

    private fun updateLastUpdateTime(date: Date) {
        _uiState.update {
            it.copy(
                lastUpdate = date
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
                            lastUpdate = note.lastUpdate,
                            photoPaths = note.photoPaths,
                            isArchived = note.isArchived,
                            isDeleted = note.isDeleted,
                            position = note.position
                        )
                    }
                    initState = EditNoteState(
                        noteId = note.id,
                        title = note.title,
                        content = note.content,
                        pinned = note.pinned,
                        isLoading = false,
                        lastUpdate = note.lastUpdate,
                        photoPaths = note.photoPaths,
                        isArchived = note.isArchived,
                        isDeleted = note.isDeleted,
                        position = note.position
                    )
                    Log.d("HAHAHH2", "pos ${initState.position}")

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