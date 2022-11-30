package com.example.notestask.Adaptador


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notestask.Entidades.Imagenes
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_vista_imagenes.view.*

class AdaptadorImagenes : RecyclerView.Adapter<AdaptadorImagenes.ImagesViewHolder>() {
    class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var listener: OnItemClickListener? = null
    var arrimagenes = ArrayList<Imagenes>()

    interface OnItemClickListener {
        fun onClicked(idImg: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.f_vista_imagenes, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return arrimagenes.size
    }

    fun setData(arrlistImagenes: List<Imagenes>) {
        arrimagenes = arrlistImagenes as ArrayList<Imagenes>
    }



    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.itemView.imageViewN.text=arrimagenes[position].uri
        holder.itemView.idImagen.text = arrimagenes[position].idImg.toString()
        holder.itemView.idNota.text = arrimagenes[position].idNFK.toString()
        holder.itemView.idTipoN.text = arrimagenes[position].tipo
        holder.itemView.cardViewN.setOnClickListener {
            listener!!.onClicked(arrimagenes[position].idImg!!)
        }

    }

}