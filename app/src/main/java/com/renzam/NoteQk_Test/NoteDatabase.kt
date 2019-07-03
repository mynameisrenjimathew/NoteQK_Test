package com.renzam.NoteQk_Test

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    private class PopulateDbAsyncTask(db: NoteDatabase) : AsyncTask<Void, Void, Void>() {

        private val noteDao: NoteDao

        init {
            noteDao = db.noteDao()

        }

        override fun doInBackground(vararg voids: Void): Void? {
            noteDao.insert(Note("Title 1", "description 1", 1))
            noteDao.insert(Note("Title 2", "description 2", 2))
            noteDao.insert(Note("Title 3", "description 3", 3))
            noteDao.insert(Note("Title 5", "description 7", 10))
            noteDao.insert(Note("Title 6", "description 8", 2))
            noteDao.insert(Note("Title 7", "description 9", 3))
            return null
        }
    }

    companion object {

        private var instance: NoteDatabase? = null


        @Synchronized
        fun getInstance(context: Context): NoteDatabase {

            if (instance == null) {

                instance = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "note_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
            }
            return instance as NoteDatabase
        }

        private val roomCallback = object : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                PopulateDbAsyncTask(instance!!).execute()

            }
        }
    }
}
