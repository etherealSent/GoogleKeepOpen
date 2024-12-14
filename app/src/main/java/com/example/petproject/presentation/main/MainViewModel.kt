package com.example.petproject.presentation.main

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petproject.R
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.entities.tag.Tag
import com.example.petproject.domain.usecases.note.ArchiveNotesUseCase
import com.example.petproject.domain.usecases.note.DeleteNotesUseCase
import com.example.petproject.domain.usecases.note.ObserveNotesWithTagsUseCase
import com.example.petproject.domain.usecases.note.PinNotesUseCase
import com.example.petproject.domain.usecases.note.SaveNoteUseCase
import com.example.petproject.domain.usecases.note.UpdateNoteColorUseCase
import com.example.petproject.domain.usecases.note.UpdateNotesPositionsUseCase
import com.example.petproject.domain.usecases.tag.ObserveTagsUseCase
import com.example.petproject.presentation.mappers.NoteToDomainMapper
import com.example.petproject.presentation.mappers.NoteWithTagsToUiMapper
import com.example.petproject.presentation.mappers.TagToUiMapper
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.utils.Async
import com.example.petproject.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val noteWithTagsToUiMapper: NoteWithTagsToUiMapper,
    private val noteToDomainMapper: NoteToDomainMapper = NoteToDomainMapper(),
    private val tagToUiMapper: TagToUiMapper,
    private val observeNotesWithTagsUseCase: ObserveNotesWithTagsUseCase,
    private val pinNotesUseCase: PinNotesUseCase,
    private val observeTagsUseCase: ObserveTagsUseCase,
    private val archiveNotesUseCase: ArchiveNotesUseCase,
    private val deleteNotesUseCase: DeleteNotesUseCase,
    private val updateNotesPositionsUseCase: UpdateNotesPositionsUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val updateNoteColorUseCase: UpdateNoteColorUseCase
    ) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _uiMainState = MutableStateFlow(UiMainState())

    private val _notes = observeNotesWithTagsUseCase
        .observeNotesWithTags()
        .map { Async.Success(it) }
        .catch<Async<List<NoteWithTags>>> { emit(Async.Error(R.string.loading_tasks_error)) }

    private val _tags = observeTagsUseCase
        .getStreamTags()
        .map { Async.Success(it) }
        .catch<Async<List<Tag>>> { emit(Async.Error(R.string.loading_tasks_error)) }

    val uiState: StateFlow<MainState> = combine(
        _isLoading, _notes, _tags, _uiMainState
    )  { isLoading, notes, tags, uiMainState ->
        when(notes) {
            is Async.Loading -> {
                MainState(isLoading = true)
            }
            is Async.Error -> MainState()
            is Async.Success -> {
                Log.d("MainViewModel", "uiState: ${notes.data}")

                val notesWithTags = notes.data.map(noteWithTagsToUiMapper)
                val tagsData: List<TagUi>
                when(tags) {
                    is Async.Success -> {
                        tagsData = tags.data.map(tagToUiMapper)
                    }
                    else -> {
                        tagsData = emptyList()
                    }

                }
                MainState(
                    notesWithTags = notes.data.map { noteWithTagsToUiMapper(it) },
                    isLoading = false,
                    tags = tagsData,
                    uiMainState = uiMainState
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MainState(isLoading = true)
    )

    fun changeScreenType(screenType: MainScreenType) {
        _uiMainState.update {
            it.copy(
                screenType = screenType
            )
        }
    }

    fun changeNotesViewType() {
        _uiMainState.update {
            it.copy(
                notesViewType = if (it.notesViewType == NotesViewType.Column) NotesViewType.Grid else NotesViewType.Column
            )
        }
    }

    fun onColorPicked(color: Color) {
        val selectedNote = _uiMainState.value.notesSelected[0]
        viewModelScope.launch {
            updateNoteColorUseCase.updateNoteColor(
                noteToDomainMapper(selectedNote), color.toArgb()
            )
            showColorDialog()
            resetSelectedNotes()
        }
        Log.d("MainViewModel", "onColorPicked: ${color.toArgb()}")
    }

    fun showColorDialog() {
        _uiMainState.update {
            it.copy(
               showColorDialog = !_uiMainState.value.showColorDialog,
                pickedColor = if(it.notesSelected.isNotEmpty()) it.notesSelected[0].color else Color.Transparent
            )
        }
    }

    fun selectTag(tagUi: TagUi) {
        _uiMainState.update {
            it.copy(
                selectedTagUi = tagUi
            )
        }
    }

    fun selectNavDrawer(idx: Int) {
        _uiMainState.update {
            it.copy(
                selectedIndex = idx
            )
        }
    }

    fun onNoteSelected(noteUi: NoteUi) {
        if (_uiMainState.value.notesSelected.contains(noteUi)) {
            if (_uiMainState.value.notesSelected.size.dec() == 0) {
                _uiMainState.update {
                    it.copy(
                        noteSelected = false,
                        notesSelected = it.notesSelected - noteUi
                    )
                }
            } else {
                _uiMainState.update {
                    it.copy(
                        notesSelected = it.notesSelected - noteUi
                    )
                }
            }
        } else {
            _uiMainState.update {
                it.copy(
                    noteSelected = true,
                    notesSelected = it.notesSelected + noteUi,
                )
            }
        }
    }

    fun closeNoteSelection() {
        _uiMainState.update {
            it.copy(
                noteSelected = false,
                notesSelected = emptyList()
            )
        }
    }

    private fun resetSelectedNotes() {
        _uiMainState.update {
            it.copy(
                noteSelected = false,
                notesSelected = emptyList()
            )
        }
    }

    fun expandMenu() {
        _uiMainState.update {
            it.copy(
                menuExpanded = !it.menuExpanded
            )
        }
    }

    fun archiveNotes() {
        viewModelScope.launch {
            archiveNotesUseCase.archiveNotes(_uiMainState.value.notesSelected.map { it.id })
            resetSelectedNotes()
        }
    }

    fun deleteNotes() {
        viewModelScope.launch {
            deleteNotesUseCase.deleteNotes(_uiMainState.value.notesSelected.map { it.id })
            resetSelectedNotes()
        }
    }

    fun copyNote() {
        viewModelScope.launch {
            updateNotesPositionsUseCase.incNotesPositions(
                fromPosition = 1,
                pinned = false
            )

            saveNoteUseCase.saveNote(
                noteToDomainMapper(
                    _uiMainState.value.notesSelected[0].run {
                        NoteUi(id = "", title, content, listOf(), false, lastUpdate, photoPaths, isArchived, isDeleted, 1, color)
                    }
                )
            )
            resetSelectedNotes()
        }
    }

    fun pinNotes() {
        viewModelScope.launch {
            val notes = _uiMainState.value.notesSelected.map { noteToDomainMapper(it) }
            pinNotesUseCase.pinNotes(notes,  _uiMainState.value.notesSelected.any { !it.pinned })
            resetSelectedNotes()
        }
    }
}
