package com.wenubey.di

import com.wenubey.data.NoteDataSource
import com.wenubey.data.NoteDataSourceImpl
import com.wenubey.notes.NotesController
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import kotlin.coroutines.CoroutineContext


val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("notes_db")
    }

    single<CoroutineDispatcher> { Dispatchers.IO }

    single<NoteDataSource> { NoteDataSourceImpl(get(), get()) }

    single { NotesController(get()) }
}