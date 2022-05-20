package dev.snippets.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.snippets.data.models.Snippet
import dev.snippets.data.models.SnippetsTagsCrossRef
import dev.snippets.data.models.Tag

@Database(entities = [Snippet::class, Tag::class, SnippetsTagsCrossRef::class], version = 1)
@TypeConverters(Converters::class)
abstract class SnippetsDatabase : RoomDatabase() {
    abstract fun snippetsDao(): SnippetsDao
}