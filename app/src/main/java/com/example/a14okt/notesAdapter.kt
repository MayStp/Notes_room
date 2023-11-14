package com.example.a14okt;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a14okt.database.Notes

class NotesAdapter(private val notesList: List<Notes>,
                     private val onItemClick: (Notes) -> Unit,
                        private val onItemLongClick: (Notes) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
        }

        override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = notesList[position]
        holder.title.text = currentNote.title
        holder.date.text = currentNote.date
        holder.description.text = currentNote.description
        }

        override fun getItemCount(): Int {
        return notesList.size
        }

        inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val title: TextView = itemView.findViewById(R.id.itemTitle)
                val date: TextView = itemView.findViewById(R.id.itemDate)
                val description: TextView = itemView.findViewById(R.id.itemDescription)

                init {
                        itemView.setOnClickListener {
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                        onItemClick(notesList[position])
                                }
                        }

                        itemView.setOnLongClickListener {
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                        onItemLongClick(notesList[position])
                                        true
                                } else {
                                        false
                                }
                        }
                }
        }
}
