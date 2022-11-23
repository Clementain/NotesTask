package com.example.notestask.Fragmentos

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Notas
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_crear_notas.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*

class FragmentoCrearNotas : FragmentoBase(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    var selectedColor = "#171C26"
    var currentDate: String? = null
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE = 456
    private var selectedImagePath = ""
    private var webLink = ""
    private var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteId = requireArguments().getInt("noteId", -1)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.f_crear_notas, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentoCrearNotas().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (noteId != -1) {

            launch {
                context?.let {
                    var notas = BaseDatosNotas.getBaseDatos(it).dAONotas().obtenerNota(noteId)
                    cTitulo.setText(notas.titulo)
                    cDesc.setText(notas.descripcion)

                }
            }
        }


        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        currentDate = sdf.format(Date())

        cFecha.text = currentDate

        btnGuardar.setOnClickListener {
            if (noteId != -1) {
                actualizarNota()
            } else {
                guardarNota()
            }
        }

        btnBorrar.setOnClickListener {
            if (noteId != -1) {
                borrarNota()
            } else {
                Toast.makeText(requireContext(), "Primero guarda la nota", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        btnAtras.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }


    private fun actualizarNota() {
        launch {

            context?.let {
                var notes = BaseDatosNotas.getBaseDatos(it).dAONotas().obtenerNota(noteId)

                notes.titulo = cTitulo.text.toString()
                notes.descripcion = cDesc.text.toString()
                notes.fecha = currentDate

                BaseDatosNotas.getBaseDatos(it).dAONotas().actualizarNota(notes)
                cTitulo.setText("")
                cDesc.setText("")
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun guardarNota() {

        launch {
            var notes = Notas()
            notes.titulo = cTitulo.text.toString()
            notes.descripcion = cDesc.text.toString()
            notes.fecha = currentDate
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAONotas().insertarNota(notes)
                cTitulo.setText("")
                cDesc.setText("")
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun borrarNota() {

        launch {
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAONotas().borrarUnaNota(noteId)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


    private fun hasReadStoragePerm(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }


    private fun readStorageTask() {
        if (hasReadStoragePerm()) {


            pickImageFromGallery()
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun pickImageFromGallery() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath: String? = null
        var cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                var selectedImageUrl = data.data
                if (selectedImageUrl != null) {
                    try {
                        var inputStream =
                            requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)


                        selectedImagePath = getPathFromUri(selectedImageUrl)!!
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, requireActivity()
        )
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }

    override fun onRationaleAccepted(requestCode: Int) {

    }

}

