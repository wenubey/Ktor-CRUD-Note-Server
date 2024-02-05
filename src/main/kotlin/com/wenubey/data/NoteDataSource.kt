package com.wenubey.data

import com.wenubey.data.model.Note

interface NoteDataSource {

    suspend fun addNote(note: Note)

    suspend fun getAllNotes(): List<Note>

    suspend fun updateNote(note: Note, id: String): Boolean

    suspend fun deleteNote(id: String): Boolean

    suspend fun getNoteById(id: String): Note?
}