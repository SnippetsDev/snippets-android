package dev.snippets.data.models

import androidx.room.Entity

@Entity(tableName = "table_snippets_tags_cross_ref", primaryKeys = ["id", "tagName"])
data class SnippetsTagsCrossRef(
    val id: String, val tagName: String
)