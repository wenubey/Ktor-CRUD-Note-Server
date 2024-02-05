package com.wenubey.notes

import com.wenubey.data.NoteDataSource
import com.wenubey.data.model.Note

class NotesController(
    private val noteDataSource: NoteDataSource,
) {

    suspend fun addNote(note: Note) {
        noteDataSource.addNote(note)
    }

    suspend fun getAllNotes(): List<Note> {
        return noteDataSource.getAllNotes()
    }

    suspend fun updateNote(note: Note, id: String): Boolean {
        return noteDataSource.updateNote(note = note,id = id)
    }

    suspend fun getNoteById(id: String): Note? {
        return noteDataSource.getNoteById(id)
    }

    suspend fun deleteNote(id: String): Boolean {
        return noteDataSource.deleteNote(id)
    }
}