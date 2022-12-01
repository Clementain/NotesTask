package com.example.notestask.Adaptador


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notestask.Entidades.Videos
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_vista_videos.view.*

class AdaptadorVideos : RecyclerView.Adapter<AdaptadorVideos.VideosViewHolder>() {
    class VideosViewHolder(view: View) : RecyclerView.ViewHolder(view)


    var arrvideos = ArrayList<Videos>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        return VideosViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.f_vista_videos, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return arrvideos.size
    }

    fun setData(arrlistVideos: List<Videos>) {
        arrvideos = arrlistVideos as ArrayList<Videos>
    }


    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        holder.itemView.videoViewN.setVideoURI(Uri.parse(arrvideos[position].uri))
    }

}