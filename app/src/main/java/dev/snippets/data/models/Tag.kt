package dev.snippets.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Problem with tags is that they are really just plain Strings, so storage and retrieval often
 * requires mapping function
 */
@Entity(tableName = "table_tags")
data class Tag(
    @PrimaryKey val name: String
)