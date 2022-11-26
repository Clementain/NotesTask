package com.example.notestask.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notestask.Entidades.Tareas
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_vista_tareas.view.*

class AdaptadorTareas : RecyclerView.Adapter<AdaptadorTareas.TasksViewHolder>() {
    var listener: OnItemClickListener? = null
    var arrList = ArrayList<Tareas>()
    lateinit var titulo: TextView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.f_vista_tareas, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData(arrTasksList: List<Tareas>) {
        arrList = arrTasksList as ArrayList<Tareas>
    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {

        holder.itemView.TituloT.text = arrList[position].tituloT
        holder.itemView.DescT.text = arrList[position].descripcionT
        holder.itemView.FechaT.text = arrList[position].fechaT
        holder.itemView.fechaCumplir.text=arrList[position].fechaCumplirT


        holder.itemView.cardViewT.setOnClickListener {
            listener!!.onClicked(arrList[position].idT!!)
        }

    }

    class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view)


    interface OnItemClickListener {
        fun onClicked(noteId: Int)
    }

}