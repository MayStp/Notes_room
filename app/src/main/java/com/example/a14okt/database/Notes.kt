package com.example.a14okt.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int=0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "date")
    val date: String
)
