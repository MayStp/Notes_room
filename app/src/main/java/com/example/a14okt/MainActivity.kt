package com.example.a14okt

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.a14okt.database.Notes
import com.example.a14okt.database.NotesDao
import com.example.a14okt.database.NotesRoomDatabase
import com.example.a14okt.databinding.ActivityMainBinding
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNotesDao: NotesDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int = 0
    private val REQUEST_CODE_SECOND_ACTIVITY = 1

    private val listNotes = mutableListOf<Notes>() // Tambahkan list untuk menyimpan data yang akan ditampilkan
    private lateinit var notesAdapter: NotesAdapter // Deklarasikan adapter di sini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        executorService = Executors.newSingleThreadExecutor()
        val db = NotesRoomDatabase.getDatabase(this)
        mNotesDao = db!!.notesDao()!!
        setContentView(binding.root)

        notesAdapter = NotesAdapter(listNotes,
            onItemClick = { item ->
                // Hanya perbarui variabel 'updateId', tanpa mengubah input form
                updateId = item.id
            },
            onItemLongClick = { item ->
                delete(item)
            }
        )
        binding.recyclerView.adapter = notesAdapter // Set adapter ke RecyclerView di sini

        binding.btnnn.setOnClickListener {
            val intent = Intent(this, Second::class.java)
            startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY) // Ubah ke startActivityForResult
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY && resultCode == Activity.RESULT_OK) {

        }
    }

    private fun getAll() {
        mNotesDao.getAllNotes.removeObservers(this) // Hapus observer sebelum menambahkan yang baru
        mNotesDao.getAllNotes.observe(this) { notes ->
            Log.d("MainActivity", "getAll: Observing notes")
            listNotes.clear()
            listNotes.addAll(notes)
            notesAdapter.notifyDataSetChanged()
        }
    }


    private fun insert(notes: Notes){
        executorService.execute {
            mNotesDao.insertNotes(notes)
            val intent = Intent() // Mengirim kembali data yang baru dimasukkan
             // Mengirim data yang baru dimasukkan
            setResult(Activity.RESULT_OK, intent)
        }
    }

    private fun update(notes: Notes){
        executorService.execute { mNotesDao.updateNotes(notes) }
    }

    private fun delete(notes: Notes){
        executorService.execute { mNotesDao.deleteNotes(notes) }
    }

    override fun onResume(){
        super.onResume()
        getAll()
        Log.d("MainActivity", "onResume: getAll() called")
    }
}