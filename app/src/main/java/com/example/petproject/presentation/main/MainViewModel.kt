package com.example.petproject.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petproject.R
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.entities.tag.Tag
import com.example.petproject.domain.usecases.note.ObserveNotesUseCase
import com.example.petproject.domain.usecases.note.ObserveNotesWithTagsUseCase
import com.example.petproject.domain.usecases.note.SaveNoteUseCase
import com.example.petproject.domain.usecases.tag.ObserveTagsUseCase
import com.example.petproject.presentation.mappers.NoteToDomainMapper
import com.example.petproject.presentation.mappers.NoteToUiMapper
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val noteWithTagsToUiMapper: NoteWithTagsToUiMapper,
    private val tagToUiMapper: TagToUiMapper,
    private val observeNotesWithTagsUseCase: ObserveNotesWithTagsUseCase,
    private val observeTagsUseCase: ObserveTagsUseCase
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

}

// all fixed
// new gets new pos