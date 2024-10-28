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

    private val _newTagName = MutableStateFlow("")
    val newTagName = _newTagName.asStateFlow()

    private val _creationMessage = MutableStateFlow("")
    val creationMessage = _creationMessage.asStateFlow()


    val uiState: StateFlow<EditTagsState> = combine(_isLoading, _tags) { isLoading, tags ->
        when(tags) {
            is Async.Success -> EditTagsState(
                tags = tags.data.map { tagToUiMapper(it) },
                isLoading = isLoading,
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
        viewModelScope.launch {
            updateTagUseCase.updateTag(
                tagToDomainMapper(
                    uiState.value.tags.find { it.id == tagId } ?: TagUi("", "")
                )
            )
        }
    }

    fun onNewTagNameChanged(newTagName: String) {
        _newTagName.value = newTagName
    }

    fun createTag() {
        if (checkTag()) {
            _creationMessage.value = "Этот ярлык уже существует"
        } else {
            viewModelScope.launch {
                saveTagUseCase.saveTag(Tag(name = newTagName.value))
            }
            clearNewTag()
        }
    }

    fun clearNewTag() {
        _newTagName.value = ""
    }



    fun checkTag(): Boolean {
        return uiState.value.tags.map { it.name }.contains(newTagName.value)
    }
}