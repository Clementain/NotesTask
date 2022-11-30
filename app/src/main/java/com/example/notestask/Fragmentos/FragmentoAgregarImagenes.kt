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
import com.example.notestask.Entidades.Imagenes
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_agregar_imagenes.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FragmentoAgregarImagenes : FragmentoBase() {
    private var idN = -1
    private var urImagen: Uri? = null
    private val SELECT_ACTIVITY = 50
    private val TAKE_ACTIVITY = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idN = requireArguments().getInt("idN", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_agregar_imagenes, container, false)

    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentoAgregarImagenes().apply {
            arguments = Bundle().apply { }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAgregarFotoCN.setOnClickListener {

            Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI
            ).also { pickPictureIntent ->
                pickPictureIntent.resolveActivity(requireActivity().packageManager).also {
                    val imageFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    imageFile?.also {
                        urImagen = FileProvider.getUriForFile(
                            requireContext(), "com.example.notestask.fileprovider", it
                        )

                        startActivityForResult(pickPictureIntent, SELECT_ACTIVITY)
                    }
                }
            }

        }

        btnAgregarFotoCamaraCN.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {

                        null
                    }
                    photoFile?.also {
                        urImagen = FileProvider.getUriForFile(
                            requireContext(), "com.example.notestask.fileprovider", it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, urImagen)
                        startActivityForResult(takePictureIntent, TAKE_ACTIVITY)
                    }
                }

            }
        }

        btnGuardarCN.setOnClickListener {
            guardarImagen()
        }
        btnAtrasCN.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun guardarImagen() {
        launch {
            var imgs = Imagenes()
            imgs.uri = urImagen.toString()
            imgs.tipo = tipoCN.text.toString()
            imgs.idNFK = idN
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAOImagenes().insertarImagen(imgs)
                requireActivity().supportFragmentManager.popBackStack()

            }
        }
    }

    lateinit var currentPhotoPath: String
    lateinit var currentVideoPath: String

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir: File? = context?.filesDir
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            currentVideoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK) {
            mostrarFotoCN.setImageURI(urImagen)
        } else if (requestCode == TAKE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            mostrarFotoCN.setImageURI(urImagen)
        }
    }
}
