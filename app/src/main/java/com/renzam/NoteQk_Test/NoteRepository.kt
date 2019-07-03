package com.renzam.NoteQk_Test

import android.app.Application
import android.os.AsyncTask

import androidx.lifecycle.LiveData

class NoteRepository(application: Application) {

    private val noteDao: NoteDao
    val allNotes: LiveData<List<Note>>

    init {

        val database = NoteDatabase.getInstance(application)
        noteDao = database.noteDao()
        allNotes = noteDao.allNotes

    }

    fun insert(note: Note) {
        InsertNoteAsyncTask(noteDao).execute(note)

    }

    fun update(note: Note) {
        UpdateNoteAsyncTask(noteDao).execute(note)

    }

    fun delete(note: Note) {

        DeletetNoteAsyncTask(noteDao).execute(note)

    }

    fun deleteAllNotes() {

        DeleteAllNoteAsyncTask(noteDao).execute()

    }

    class InsertNoteAsyncTask(private val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.insert(notes[0])
            return null
        }
    }

    class UpdateNoteAsyncTask(private val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.update(notes[0])
            return null
        }
    }

    class DeletetNoteAsyncTask(private val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {

        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.delete(notes[0])
            return null
        }
    }

    class DeleteAllNoteAsyncTask(private val noteDao: NoteDao) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            noteDao.deleteAllNotes()
            return null
        }
    }
}
