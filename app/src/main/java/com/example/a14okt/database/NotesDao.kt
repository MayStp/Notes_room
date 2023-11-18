package com.example.a14okt.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNotes(notes: Notes)

    @Update
    fun updateNotes(notes: Notes)

    @Delete
    fun deleteNotes(notes: Notes)

    @Query("SELECT * FROM catatans_table WHERE id = :noteId")
    fun getNoteById(noteId: Int): LiveData<Notes>


    @get:Query("SELECT * FROM catatans_table ORDER BY id ASC")
    val getAllNotes: LiveData<List<Notes>>

}
