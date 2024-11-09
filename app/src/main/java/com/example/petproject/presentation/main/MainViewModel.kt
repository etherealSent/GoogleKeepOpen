package com.example.petproject.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petproject.R
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.usecases.note.ObserveNotesUseCase
import com.example.petproject.domain.usecases.note.ObserveNotesWithTagsUseCase
import com.example.petproject.domain.usecases.note.SaveNoteUseCase
import com.example.petproject.presentation.mappers.NoteToDomainMapper
import com.example.petproject.presentation.mappers.NoteToUiMapper
import com.example.petproject.presentation.mappers.NoteWithTagsToUiMapper
import com.example.petproject.presentation.model.NoteUi
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
    private val observeNotesWithTagsUseCase: ObserveNotesWithTagsUseCase
    ) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _screenType = MutableStateFlow(MainScreenType.Notes)

    private val _notes = observeNotesWithTagsUseCase
        .observeNotesWithTags()
        .map { Async.Success(it) }
        .catch<Async<List<NoteWithTags>>> { emit(Async.Error(R.string.loading_tasks_error)) }

    val uiState: StateFlow<MainState> = combine(
        _isLoading, _notes, _screenType
    )  { isLoading, notes, screenType ->
        when(notes) {
            is Async.Loading -> {
                MainState(isLoading = true)
            }
            is Async.Error -> MainState()
            is Async.Success -> MainState(
                notesWithTags = notes.data.map { noteWithTagsToUiMapper(it) }.filter { !it.isArchived && !it.isDeleted },
                isLoading = false,
                screenType = screenType
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MainState(isLoading = true)
    )

    fun changeScreenType(screenType: MainScreenType) {
        _screenType.value = screenType
    }

}