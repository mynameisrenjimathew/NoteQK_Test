package com.renzam.NoteQk_Test

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var noteViewModel: NoteViewModel? = null
    //internal var recyclerView: RecyclerView
    internal lateinit var recyclerView: RecyclerView

    //internal var floatingActionButton: FloatingActionButton
    internal lateinit var floatingActionButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "NoteQK_Test"

        floatingActionButton = findViewById(R.id.floatingActionButton)

        floatingActionButton.setOnClickListener { startActivityForResult(Intent(applicationContext, AddEditNoteActivity::class.java), AddNoteReqst) }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val noteAdapter = NoteAdapter()
        recyclerView.adapter = noteAdapter

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel!!.allNotes.observe(this as LifecycleOwner, Observer { notes ->
            //update Recycler
            noteAdapter.setNotes(notes)
        })
        //recycler on Swipable
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                noteViewModel!!.delete(noteAdapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note Deleted", Toast.LENGTH_SHORT).show()

            }
        }).attachToRecyclerView(recyclerView)

        noteAdapter.setOnItemClickListener(object : NoteAdapter.OnitemClickListener {
            override fun onitemClick(note: Note) {
                val intent = Intent(applicationContext, AddEditNoteActivity::class.java)
                intent.putExtra(AddEditNoteActivity.EXSTRA_ID, note.id)
                intent.putExtra(AddEditNoteActivity.EXSTRA_TITLE, note.title)
                intent.putExtra(AddEditNoteActivity.EXSTRA_DESCRIPTION, note.description)
                intent.putExtra(AddEditNoteActivity.EXSTRA_PRIORITY, note.priority)
                startActivityForResult(intent, EditNoteRequst)
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddNoteReqst && resultCode == Activity.RESULT_OK) {

            val title = data!!.getStringExtra(AddEditNoteActivity.EXSTRA_TITLE)
            val description = data.getStringExtra(AddEditNoteActivity.EXSTRA_DESCRIPTION)
            val priority = data.getIntExtra(AddEditNoteActivity.EXSTRA_PRIORITY, 1)

            val note = Note(title, description, priority)
            noteViewModel!!.insert(note)

            Toast.makeText(this, "Note Saved :)", Toast.LENGTH_SHORT).show()

        } else if (requestCode == EditNoteRequst && resultCode == Activity.RESULT_OK) {

            val id = data!!.getIntExtra(AddEditNoteActivity.EXSTRA_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Cant edit", Toast.LENGTH_SHORT).show()
                return
            }
            val title = data.getStringExtra(AddEditNoteActivity.EXSTRA_TITLE)
            val description = data.getStringExtra(AddEditNoteActivity.EXSTRA_DESCRIPTION)
            val priority = data.getIntExtra(AddEditNoteActivity.EXSTRA_PRIORITY, 1)

            val note = Note(title, description, priority)
            note.id = id
            noteViewModel!!.update(note)
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()


        } else {
            Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if (item.itemId == R.id.deleteAllNotesId) {

            noteViewModel!!.deleteAllNotes()
            return true

        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val AddNoteReqst = 1
        val EditNoteRequst = 2
    }
}
