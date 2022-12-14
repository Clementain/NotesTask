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
import android.widget.Toast
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Audios
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_agregar_audios.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class FragmentoAgregarAudio : FragmentoBase() {
    private var idN = -1
    private var tipo = -1
    private var auUri: String = ""
    private var aId = -1
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var mStartRecording: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idN = requireArguments().getInt("idNA", -1)
        tipo = requireArguments().getInt("tipo", -1)
        aId = requireArguments().getInt("aId", -1)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_agregar_audios, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentoAgregarAudio().apply {
            arguments = Bundle().apply { }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (aId != -1) {

            launch {
                context?.let {
                    var auds = BaseDatosNotas.getBaseDatos(it).dAOAudios().obtenerAudio(aId)
                    auUri = auds.uri.toString()
                }
            }
        }

        btnGuardarAudioCN.setOnClickListener {
            if (aId != -1) {
                actualizaraudio()
            } else {
                guardarAudio()
            }
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
        btnBorrarA.setOnClickListener {
            if (aId != -1) {
                borrarAudio()
            } else {
                Toast.makeText(requireContext(), "Primero guarda el audio", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun borrarAudio() {
        launch {
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAOAudios().borrarAudio(aId)
                requireActivity().supportFragmentManager.popBackStack()
            }
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

    private fun actualizaraudio() {
        launch {
            context?.let {
                var auds = BaseDatosNotas.getBaseDatos(it).dAOAudios().obtenerAudio(aId)
                auds.uri = auUri

                BaseDatosNotas.getBaseDatos(it).dAOAudios().actualizarAudio(auds)
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
        txtStart.text = "Presiona el boton para iniciar la grabaci??n"
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
        txtStart.text = "Presiona el boton para detener la grabaci??n"
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