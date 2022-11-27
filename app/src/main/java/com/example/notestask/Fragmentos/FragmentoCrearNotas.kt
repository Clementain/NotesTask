package com.example.notestask.Fragmentos

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.notestask.Adaptador.ImageControler
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Notas
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_crear_notas.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentoCrearNotas : FragmentoBase() {

    var currentDate: String? = null
    private val SELECT_ACTIVITY=50
    private var urImagen: Uri?=null
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
                    val urimagen=ImageControler.getImageUri(super.requireContext(),"N$noteId")
                    mostrarFoto.setImageURI(urimagen)
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

        btnAgregarFoto.setOnClickListener {
            ImageControler.selectPhotoFromGallery(this,SELECT_ACTIVITY)
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
                urImagen?.let{
                    ImageControler.saveImage(super.requireContext(),noteId.toLong(),it,"N")
                }
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
                val id= BaseDatosNotas.getBaseDatos(it).dAONotas().obtenerId()
             BaseDatosNotas.getBaseDatos(it).dAONotas().insertarNota(notes)
                urImagen?.let{
                    ImageControler.saveImage(super.requireContext(),id!!.toLong()+1,it,"N")
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode==SELECT_ACTIVITY&&resultCode==Activity.RESULT_OK->{
            urImagen=data!!.data
                mostrarFoto.setImageURI(urImagen)
            }
        }
    }
}

