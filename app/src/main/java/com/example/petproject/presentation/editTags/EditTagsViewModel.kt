package com.example.petproject.presentation.editTags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petproject.R
import com.example.petproject.domain.entities.tag.Tag
import com.example.petproject.domain.usecases.tag.ObserveTagsUseCase
import com.example.petproject.domain.usecases.tag.SaveTagUseCase
import com.example.petproject.domain.usecases.tag.UpdateTagUseCase
import com.example.petproject.presentation.mappers.TagToDomainMapper
import com.example.petproject.presentation.mappers.TagToUiMapper
import com.example.petproject.presentation.model.TagUi
import com.example.petproject.utils.Async
import com.example.petproject.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditTagsEvent {
    data class TagUpdated(val tag: Tag) : EditTagsEvent()
}

@HiltViewModel
class EditTagsViewModel @Inject constructor(
    private val observeTagsUseCase : ObserveTagsUseCase,
    private val tagToUiMapper: TagToUiMapper = TagToUiMapper(),
    private val updateTagUseCase: UpdateTagUseCase,
    private val tagToDomainMapper: TagToDomainMapper = TagToDomainMapper(),
    private val saveTagUseCase: SaveTagUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _tags = observeTagsUseCase
        .getStreamTags()
        .map { Async.Success(it) }
        .catch<Async<List<Tag>>> { emit(Async.Error(R.string.loading_tasks_error)) }


    private val _screenState = MutableStateFlow(EditTagsScreenState())
    val screenState = _screenState.asStateFlow()

    val uiState: StateFlow<EditTagsState> = combine(_isLoading, _tags, _screenState) { isLoading, tags, screenState ->
        when(tags) {
            is Async.Success -> EditTagsState(
                tags = tags.data.map { tagToUiMapper(it) },
                isLoading = isLoading,
                newTagName = screenState.newTagName,
                editingId = screenState.editingId,
            )
            is Async.Loading -> EditTagsState(isLoading = true)
            is Async.Error -> EditTagsState()
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = EditTagsState(isLoading = true)
    )

    fun onTagEdit(tagId: String) {
        _screenState.update {
            it.copy(editingId = tagId)
        }
    }

    fun onTagChanged(tag: TagUi, newTagName: String) {
        viewModelScope.launch {
            updateTagUseCase.updateTag(
                tagToDomainMapper(tag.copy(name = newTagName))
            )
        }
    }

    fun onNewTagNameChanged(newTagName: String) {
        _screenState.update {
            it.copy(newTagName = newTagName)
        }
    }

    fun createTag() {
        if (checkTag()) {
//            _creationMessage.value = "Этот ярлык уже существует"
        } else {
            viewModelScope.launch {
                saveTagUseCase.saveTag(Tag(name = screenState.value.newTagName))
            }
            clearNewTag()
        }
    }

    fun clearNewTag() {
        _screenState.update {
            it.copy(newTagName = "", editingId = "")
        }
    }

    fun checkTag(): Boolean {
        return uiState.value.tags.map { it.name }.contains(screenState.value.newTagName)
    }
}