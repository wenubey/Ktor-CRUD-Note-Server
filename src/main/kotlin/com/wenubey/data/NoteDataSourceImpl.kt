package com.wenubey.data

import com.wenubey.data.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Implementation of [NoteDataSource] that uses a CoroutineDatabase for storing and retrieving notes.
 *
 * @property db The CoroutineDatabase instance.
 * @property ioDispatcher The CoroutineDispatcher for performing I/O operations.
 */
class NoteDataSourceImpl(
    private val db: CoroutineDatabase,
    private val ioDispatcher: CoroutineDispatcher,
) : NoteDataSource {

    private val notes = db.getCollection<Note>()
    private val logger: Logger = LoggerFactory.getLogger(NoteDataSourceImpl::class.java)

    /**
     * Adds a new note to the database.
     *
     * @param note The note to be added.
     */
    override suspend fun addNote(note: Note) {
        withContext(ioDispatcher) {
            try {
                notes.insertOne(note)
            } catch (e: Exception) {
                logger.error("Error occurred while adding a note", e)
                // Handle the exception or return a default value
            }
        }
    }

    /**
     * Retrieves all notes from the database, sorted by creation date in descending order.
     *
     * @return A list of all notes in descending order of creation date.
     */
    override suspend fun getAllNotes(): List<Note> {
        return withContext(ioDispatcher) {
            try {
                notes.find()
                    .descendingSort(Note::createdAt)
                    .toList()
            } catch (e: Exception) {
                logger.error("Error occurred while retrieving all notes", e)
                emptyList() // Return an empty list or handle the error in a meaningful way
            }
        }
    }

    /**
     * Retrieves a note by its unique identifier.
     *
     * @param id The unique identifier of the note.
     * @return The note with the specified ID, or null if not found.
     */
    override suspend fun getNoteById(id: String): Note? {
        return withContext(ioDispatcher) {
            try {
                notes.findOneById(id)
            } catch (e: Exception) {
                logger.error("Error occurred while retrieving a note by ID", e)
                null // Return null or handle the error in a meaningful way
            }
        }
    }

    /**
     * Updates an existing note in the database.
     *
     * @param note The updated note.
     * @param id The unique identifier of the note to be updated.
     * @return `true` if the note was successfully updated, `false` otherwise.
     */
    override suspend fun updateNote(note: Note, id: String): Boolean {
        return withContext(ioDispatcher) {
            try {
                val updateResult = notes.updateOneById(id, note)
                updateResult.matchedCount > 0
            } catch (e: Exception) {
                logger.error("Error occurred while updating a note", e)
                false // Return false or handle the error in a meaningful way
            }
        }
    }

    /**
     * Deletes a note from the database by its unique identifier.
     *
     * @param id The unique identifier of the note to be deleted.
     * @return `true` if the note was successfully deleted, `false` otherwise.
     */
    override suspend fun deleteNote(id: String): Boolean {
        return withContext(ioDispatcher) {
            try {
                val deleteResult = notes.deleteOneById(id)
                deleteResult.deletedCount > 0
            } catch (e: Exception) {
                logger.error("Error occurred while deleting a note", e)
                false // Return false or handle the error in a meaningful way
            }
        }
    }
}