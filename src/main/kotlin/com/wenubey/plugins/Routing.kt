package com.wenubey.plugins

import com.wenubey.data.model.Note
import com.wenubey.notes.NotesController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Configures the routing for the application, including routes related to notes.
 */
fun Application.configureRouting() {

    val notesController: NotesController by inject()

    routing {
        notesRoute(notesController)
    }
}

/**
 * Defines routes related to notes and their handling.
 *
 * @param notesController The [NotesController] for handling note-related operations.
 */
fun Route.notesRoute(notesController: NotesController) {
    route("/notes") {

        /**
         * Retrieves a note by its unique identifier.
         * Responds with the note if found, otherwise returns a 404 response.
         */
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing id.")
            try {
                val note = notesController.getNoteById(id)
                if (note != null) {
                    call.respond(note)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Note not found.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
            }
        }

        /**
         * Retrieves all notes.
         * Responds with a list of notes or a 500 response in case of an error.
         */
        get {
            try {
                val notes = notesController.getAllNotes()
                call.respond(notes)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
            }
        }

        /**
         * Adds a new note.
         * Responds with the created note or a 500 response in case of an error.
         */
        post {
            try {
                val note = call.receive<Note>()
                notesController.addNote(note)
                call.respond(HttpStatusCode.Created, note)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
            }
        }

        /**
         * Updates an existing note.
         * Responds with a success message or a 404 response if the note is not found.
         */
        put("/{id}") {
            try {
                val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Missing id.")
                val note = call.receive<Note>()
                if (notesController.updateNote(note = note, id = id)) {
                    call.respond(HttpStatusCode.OK, "Note updated successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Note not found.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
            }
        }

        /**
         * Deletes a note by its unique identifier.
         * Responds with a success message or a 404 response if the note is not found.
         */
        delete("/{id}") {
            try {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id.")
                if (notesController.deleteNote(id)) {
                    call.respond(HttpStatusCode.OK, "Note deleted successfully.")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Note not found.")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Unknown error.")
            }
        }
    }
}