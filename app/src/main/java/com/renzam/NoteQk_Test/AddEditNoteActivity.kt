package com.renzam.NoteQk_Test

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast

class AddEditNoteActivity : AppCompatActivity() {

    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var numberPickerPriority: NumberPicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        numberPickerPriority = findViewById(R.id.numberpicker)
        numberPickerPriority!!.minValue = 1
        numberPickerPriority!!.maxValue = 10

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)


        val intent = intent
        if (intent.hasExtra(EXSTRA_ID)) {
            title = "Edit Note"
            editTextTitle!!.setText(intent.getStringExtra(EXSTRA_TITLE))
            editTextDescription!!.setText(intent.getStringExtra(EXSTRA_DESCRIPTION))
            numberPickerPriority!!.value = intent.getIntExtra(EXSTRA_PRIORITY, 1)
        } else {
            title = "Add Note"

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if (item.itemId == R.id.saveNotes) {

            saveNote()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }


    }

    private fun saveNote() {

        val title = editTextTitle!!.text.toString()
        val description = editTextDescription!!.text.toString()
        val priority = numberPickerPriority!!.value

        if (title.trim { it <= ' ' }.isEmpty() || description.isEmpty()) {

            Toast.makeText(this, "Sorry Title or Description is empty", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        data.putExtra(EXSTRA_TITLE, title)
        data.putExtra(EXSTRA_DESCRIPTION, description)
        data.putExtra(EXSTRA_PRIORITY, priority)

        val id = intent.getIntExtra(EXSTRA_ID, -1)

        if (id != -1) {
            data.putExtra(EXSTRA_ID, id)
        }

        setResult(Activity.RESULT_OK, data)
        finish()


    }

    companion object {

        val EXSTRA_ID = "com.renzam.noteqk.EXSTRA_ID"
        val EXSTRA_TITLE = "com.renzam.noteqk.EXSTRA_TITLE"
        val EXSTRA_DESCRIPTION = "com.renzam.noteqk.EXSTRA_DESCRIPTION"
        val EXSTRA_PRIORITY = "com.renzam.noteqk.EXSTRA_PRIORITY"
    }
}
