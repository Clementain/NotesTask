package com.example.notestask.Fragmentos

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Videos
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_agregar_videos.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FragmentoAgregarVideos : FragmentoBase() {
    private var idN = -1
    private var viUri: Uri? = null
    private val TAKE_ACTIVITY = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idN = requireArguments().getInt("idN", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_agregar_videos, container, false)

    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentoAgregarVideos().apply {
            arguments = Bundle().apply { }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        btnAgregarVideoCamaraCN.setOnClickListener {
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                takeVideoIntent.resolveActivity(requireActivity().packageManager)?.also {
                    val videoFile: File? = try {
                        createVideoFile()
                    } catch (ex: IOException) {

                        null
                    }
                    videoFile?.also {
                        viUri = FileProvider.getUriForFile(
                            requireContext(), "com.example.notestask.fileprovider", it
                        )
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, viUri)
                        startActivityForResult(takeVideoIntent, TAKE_ACTIVITY)
                    }
                }

            }
        }

        btnGuardarCNV.setOnClickListener {
            guardarVideo()
        }
        btnAtrasCNV.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun guardarVideo() {
        launch {
            var vids = Videos()
            vids.uri = viUri.toString()
            vids.tipo = tipoCN.text.toString()
            vids.idNFK = idN
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAOVideos().insertarVideos(vids)
                requireActivity().supportFragmentManager.popBackStack()

            }
        }
    }

    lateinit var currentPhotoPath: String
    lateinit var currentVideoPath: String

    @Throws(IOException::class)
    fun createVideoFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir: File? = context?.filesDir
        return File.createTempFile(
            "VID_${timeStamp}_", /* prefix */
            ".mp4", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            currentVideoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TAKE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            mostrarVideoCN.setVideoURI(viUri)
        }
    }
}
