package com.example.a14okt

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a14okt.database.Notes
import com.example.a14okt.database.NotesDao
import com.example.a14okt.database.NotesRoomDatabase
import com.example.a14okt.databinding.ActivityMainBinding
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
        executorService = Executors.newSingleThreadExecutor()
        val db = NotesRoomDatabase.getDatabase(this)
        mNotesDao = db!!.notesDao()!!
        setContentView(binding.root)

        getAll() // Memanggil fungsi getAll() saat Activity dibuat

        // Button onClickListeners
        with(binding) {
            btnnn.setOnClickListener(View.OnClickListener {
                insert(
                    Notes(
                        0,
                        txtTitle.text.toString(),
                        txtDesc.text.toString(),
                        txtDate.text.toString()
                    )
                )
                println("run insert")
                setEmptyField()
            })

            btnUpdate.setOnClickListener {
                update(
                    Notes(
                        id = updateId,
                        txtTitle.text.toString(),
                        txtDesc.text.toString(),
                        txtDate.text.toString()
                    )
                )
                updateId = 0
                setEmptyField()
            }
        }
    }

    private fun getAll() {
        mNotesDao.getAllNotes.observe(this) { notes ->
            val adapter = NotesAdapter(notes,
                onItemClick = { item ->
                    updateId = item.id
                    binding.txtTitle.setText(item.title)
                    binding.txtDesc.setText(item.description)
                    binding.txtDate.setText(item.date)
                },
                onItemLongClick = { item ->
                    delete(item)
                }
            )
//            binding.recyclerView.adapter = adapter
        }
    }

    private fun insert(notes: Notes) {
        println("Data to be inserted: $notes")
        executorService.execute {
            mNotesDao.insertNotes(notes)
            println("Data inserted!")
            val intent = Intent() // Mengirim kembali data yang baru dimasukkan
             // Mengirim data yang baru dimasukkan
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }



    private fun update(notes: Notes) {
        executorService.execute { mNotesDao.updateNotes(notes) }
    }

    private fun delete(notes: Notes) {
        executorService.execute { mNotesDao.deleteNotes(notes) }
    }

    override fun onResume() {
        super.onResume()
        getAll()
    }

    private fun setEmptyField() {
        with(binding) {
            txtTitle.setText("")
            txtDate.setText("")
            txtDesc.setText("")
        }
    }
}