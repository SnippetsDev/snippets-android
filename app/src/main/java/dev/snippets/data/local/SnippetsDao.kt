package dev.snippets.data.local

import androidx.room.*
import dev.snippets.data.models.Snippet
import dev.snippets.data.models.SnippetsTagsCrossRef
import dev.snippets.data.models.Tag

@Dao
interface SnippetsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<Tag>)

    @Query("SELECT * FROM table_tags")
    suspend fun getAllTags(): List<Tag>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun actuallyInsertSnippets(snippets: List<Snippet>)

    @Transaction
    suspend fun insertSnippets(snippets: List<Snippet>) {
        insertTags(snippets.flatMap { it.tags }.map { Tag(it) })
        snippets.forEach { snippet -> snippet.tags.forEach { insertSnippetTagCrossRef(SnippetsTagsCrossRef(snippet.id, it)) } }
        actuallyInsertSnippets(snippets)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnippetTagCrossRef(snippetTagCrossRef: SnippetsTagsCrossRef)

    @Query("SELECT DISTINCT id FROM table_snippets_tags_cross_ref WHERE tagName IN (:tags)")
    suspend fun getSnippetIdsForTags(tags: List<String>): List<String>

    @Query("SELECT * FROM table_snippets WHERE id IN (:ids)")
    suspend fun getSnippetsByIds(ids: List<String>): List<Snippet>

    @Transaction
    suspend fun getSnippetsForTags(tags: List<String>): List<Snippet> {
        val ids = getSnippetIdsForTags(tags)
        return getSnippetsByIds(ids)
    }

    @Query("SELECT * FROM table_snippets WHERE id = :id")
    suspend fun getSnippetById(id: String): Snippet?
}