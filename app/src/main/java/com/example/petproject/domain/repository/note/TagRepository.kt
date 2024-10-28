package com.example.petproject.domain.repository.note

import com.example.petproject.data.storage.dao.TagDao
import com.example.petproject.data.storage.entities.TagDb
import com.example.petproject.data.storage.mappers.tag.TagDbToDomainMapper
import com.example.petproject.di.ApplicationScope
import com.example.petproject.di.DefaultDispatcher
import com.example.petproject.domain.entities.tag.Tag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

interface TagRepository {
    fun getTagsStream() : Flow<List<Tag>>
    suspend fun getTagById(id: String) : Tag?
    suspend fun updateTag(tag: TagDb)
    suspend fun saveTag(tag: TagDb)
}

class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao,
    private val tagDbToDomainMapper: TagDbToDomainMapper,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : TagRepository {
    override fun getTagsStream(): Flow<List<Tag>> {
        return tagDao.observeTags().map { tags ->
            withContext(dispatcher) {
                tags.map { tagDbToDomainMapper(it) }
            }
        }
    }

    override suspend fun getTagById(id: String): Tag? {
        return tagDbToDomainMapper(
            tagDao.getTagById(id) ?: TagDb("", "ha")
        )
    }

    override suspend fun updateTag(tag: TagDb) {
        tagDao.updateTag(tag)
    }

    override suspend fun saveTag(tag: TagDb) {

        val tagId = withContext(dispatcher) {
            UUID.randomUUID().toString()
        }

        tagDao.insertTag(tag.copy(tagId = tagId))
    }
}