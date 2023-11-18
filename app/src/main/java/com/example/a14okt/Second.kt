package com.example.a14okt

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a14okt.database.Notes
import com.example.a14okt.database.NotesDao
import com.example.a14okt.database.NotesRoomDatabase
import com.example.a14okt.databinding.ActivitySecondBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Second : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var mNotesDao: NotesDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NotesRoomDatabase.getDatabase(this)
        mNotesDao = db!!.notesDao()!!

        setupUI()

        val selectedNote = intent.getParcelableExtra<Notes>("SELECTED_NOTE")
        selectedNote?.let {
            updateUIWithSelectedNoteData(selectedNote)
            updateId = selectedNote.id
        }
    }

    private fun setupUI() {
        with(binding) {
            btnnn.setOnClickListener {
                insertNoteFromUI()
                setEmptyField()
            }

            btnUpdate.setOnClickListener {
                updateNoteFromUI()
                updateId = 0
                setEmptyField()
            }
        }
    }

    private fun updateUIWithSelectedNoteData(selectedNote: Notes) {
        with(binding) {
            txtTitle.setText(selectedNote.title)
            txtDesc.setText(selectedNote.description)
            txtDate.setText(selectedNote.date)
        }
    }

    private fun insertNoteFromUI() {
        val note = createNoteFromUI()
        insert(note)
    }

    private fun createNoteFromUI(): Notes {
        return Notes(
            id = 0,
            title = binding.txtTitle.text.toString(),
            description = binding.txtDesc.text.toString(),
            date = binding.txtDate.text.toString()
        )
    }

    private fun insert(notes: Notes) {
        executorService.execute {
            mNotesDao.insertNotes(notes)
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }

    private fun updateNoteFromUI() {
        val note = createNoteFromUI().copy(id = updateId)
        update(note)
    }

    private fun update(notes: Notes) {
        executorService.execute {
            mNotesDao.updateNotes(notes)
            val intent = Intent(this@Second, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setEmptyField() {
        with(binding) {
            txtTitle.setText("")
            txtDate.setText("")
            txtDesc.setText("")
        }
    }
}
