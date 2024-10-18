package com.example.petproject.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petproject.R
import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.repository.note.NoteRepository
import com.example.petproject.domain.usecases.ObserveNotesUseCase
import com.example.petproject.domain.usecases.SaveNoteUseCase
import com.example.petproject.presentation.mappers.NoteToDomainMapper
import com.example.petproject.presentation.mappers.NoteToUiMapper
import com.example.petproject.presentation.model.NoteUi
import com.example.petproject.utils.Async
import com.example.petproject.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val noteToUiMapper: NoteToUiMapper,
    private val noteToDomainMapper: NoteToDomainMapper,
    private val observeNotesUseCase: ObserveNotesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase
    ) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _notes = observeNotesUseCase
        .observeNotes()
        .map { Async.Success(it) }
        .catch<Async<List<Note>>> { emit(Async.Error(R.string.loading_tasks_error)) }

    val uiState: StateFlow<MainState> = combine(
        _isLoading, _notes
    )  { isLoading, notes ->
        when(notes) {
            is Async.Loading -> {
                MainState(isLoading = true)
            }
            is Async.Error -> MainState()
            is Async.Success -> MainState(notesWithTags = notes.data.map { noteToUiMapper(it) })
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MainState(isLoading = true)
    )

    fun createNote() {
        viewModelScope.launch {
            saveNoteUseCase.saveNote(noteToDomainMapper(NoteUi("aa", "bb", listOf(), false)))
        }
    }
}