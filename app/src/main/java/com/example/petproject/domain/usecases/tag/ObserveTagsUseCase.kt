package com.example.petproject.domain.usecases.tag

import com.example.petproject.domain.entities.tag.Tag
import com.example.petproject.domain.repository.note.TagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveTagsUseCase {
    fun getStreamTags() : Flow<List<Tag>>
}

class ObserveTagsUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository
    ) : ObserveTagsUseCase {
    override fun getStreamTags(): Flow<List<Tag>> {
        return tagRepository.getTagsStream()
    }

}