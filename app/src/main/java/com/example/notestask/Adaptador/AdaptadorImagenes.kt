package com.example.notestask.Adaptador


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notestask.Entidades.Multimedias
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_vista_imagenes.view.*

class AdaptadorImagenes : RecyclerView.Adapter<AdaptadorImagenes.ImagesViewHolder>() {
    class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var listener: OnItemClickListener? = null
    var arrimagenes = ArrayList<Multimedias>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.f_vista_imagenes, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return arrimagenes.size
    }

    fun setData(arrlistImagenes: List<Multimedias>) {
        arrimagenes = arrlistImagenes as ArrayList<Multimedias>
    }


    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.itemView.imageViewN.setImageURI(Uri.parse(arrimagenes[position].uri))
        holder.itemView.btnEditarM.setOnClickListener {
            listener!!.onClicked(arrimagenes[position].idImg!!)
        }
    }

    interface OnItemClickListener {
        fun onClicked(mId: Int)
    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

}