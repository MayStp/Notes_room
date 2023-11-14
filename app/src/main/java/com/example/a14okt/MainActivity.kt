package com.example.a14okt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        executorService = Executors.newSingleThreadExecutor()
        val db = NotesRoomDatabase.getDatabase(this)
        mNotesDao = db!!.notesDao()!!
        setContentView(binding.root)


        with(binding){
            btnnn.setOnClickListener(View.OnClickListener{
                println("running on Add")
                insert(
                    Notes(
                        0,
                        txtTitle.text.toString(),
                        txtDesc.text.toString(),
                        txtDate.text.toString()
                    )
                )
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

//    private fun getAll(){
//        println("run getAll")
//        mNotesDao.getAllNotes.observe(this) { notes ->
//            val adapter: ArrayAdapter<Notes> = ArrayAdapter<Notes>(
//                this@MainActivity, android.R.layout.simple_list_item_1, notes)
//            println("notes: $notes")
//            binding.listView.adapter = adapter
//        }
//    }
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
                binding.recyclerView.adapter = adapter
            }
        }



    private fun insert(notes: Notes){
        executorService.execute { mNotesDao.insertNotes(notes) }
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
    }

    private fun setEmptyField(){
       with(binding){
           txtTitle.setText("")
           txtDate.setText("")
           txtDesc.setText("")
       }
    }


}