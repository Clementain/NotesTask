package com.example.notestask.Adaptador

import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notestask.Entidades.Audios
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_vista_audios.view.*
import java.io.IOException

class AdaptadorAudios : RecyclerView.Adapter<AdaptadorAudios.AudsViewHolder>() {
    class AudsViewHolder(view: View) : RecyclerView.ViewHolder(view)


    var arraudios = ArrayList<Audios>()
    private var player: MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudsViewHolder {
        return AudsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.f_vista_audios, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return arraudios.size
    }

    fun setData(arrlistAudios: List<Audios>) {
        arraudios = arrlistAudios as ArrayList<Audios>
    }


    override fun onBindViewHolder(holder: AudsViewHolder, position: Int) {
        holder.itemView.imageViewA.setOnClickListener {

            onPlay(mStartPlaying, arraudios[position].uri.toString())
            mStartPlaying = !mStartPlaying
        }
    }

    private fun onPlay(start: Boolean, uri: String) = if (start) {
        startPlaying(uri)
    } else {
        stopPlaying()
    }

    private fun startPlaying(uri: String) {

        player = MediaPlayer().apply {
            try {
                setDataSource(uri)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("AudioRecordTest", "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    var mStartPlaying = true

}