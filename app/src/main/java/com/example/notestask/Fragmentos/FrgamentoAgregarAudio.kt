package com.example.notestask.Fragmentos

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Audios
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_agregar_audios.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class FrgamentoAgregarAudio : FragmentoBase() {
    private var idN = -1
    private var tipo = -1
    private var auUri: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var mStartRecording: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idN = requireArguments().getInt("idNA", -1)
        tipo = requireArguments().getInt("tipo", -1)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_agregar_audios, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FrgamentoAgregarAudio().apply {
            arguments = Bundle().apply { }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnGuardarAudioCN.setOnClickListener {
            guardarAudio()
        }
        mostrarAudioCN.setOnClickListener {
            onPlay(mStartPlaying)
            mStartPlaying = !mStartPlaying
        }
        btnAgregaAudioCN.setOnClickListener {
            onRecord(mStartRecording)
            mStartRecording = !mStartRecording
        }
        btnAtrasAudioCN.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun guardarAudio() {
        launch {
            var aud = Audios()
            aud.uri = auUri
            aud.tipo = tipo
            aud.idNFK = idN
            context?.let {

                BaseDatosNotas.getBaseDatos(it).dAOAudios().insertarAdios(aud)
                requireActivity().supportFragmentManager.popBackStack()
            }

        }

    }


    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(auUri)
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
    private fun onRecord(start: Boolean) = if (start) {
        iniciarGrabacion()
    } else {
        stopRecording()
    }

    @SuppressLint("SetTextI18n")
    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        txtStart.text="Presiona el boton para iniciar la grabación"
    }


    @SuppressLint("SetTextI18n")
    private fun iniciarGrabacion() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            createAudioFile()
            setOutputFile(auUri)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e("AudioRecordTest", "prepare() fail")

            }
            start()
        }
        txtStart.text="Presiona el boton para detener la grabación"
    }

    @Throws(IOException::class)
    fun createAudioFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        return File.createTempFile(
            "AUDIO_${timeStamp}_", /* prefix */
            ".mp3", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            auUri = absolutePath

        }
    }
}