package com.example.notetask.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notestask.Entidades.Notas
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_vista_notas.view.*

class AdaptarNotas : RecyclerView.Adapter<AdaptarNotas.NotesViewHolder>() {
    var listener: OnItemClickListener? = null
    var arrList = ArrayList<Notas>()
    lateinit var titulo: TextView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.f_vista_notas, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData(arrNotesList: List<Notas>) {
        arrList = arrNotesList as ArrayList<Notas>
    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {

        holder.itemView.Titulo.text = arrList[position].titulo
        holder.itemView.Desc.text = arrList[position].descripcion
        holder.itemView.Fecha.text = arrList[position].fecha


        holder.itemView.cardView.setOnClickListener {
            listener!!.onClicked(arrList[position].id!!)
        }

    }

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view)


    interface OnItemClickListener {
        fun onClicked(noteId: Int)
    }

}