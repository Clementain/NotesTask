package com.example.notestask.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Notas
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_crear_notas.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentoCrearNotas : FragmentoBase() {

    var currentDate: String? = null
    private var noteId = -1
    private var tipo = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteId = requireArguments().getInt("noteId", -1)
        tipo = 1


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

        btnAgregarFoto.setOnClickListener {
            if (noteId != -1) {
                var fragment: Fragment
                var bundle = Bundle()
                bundle.putInt("idN", noteId)
                bundle.putInt("tipo", tipo)
                fragment = FragmentoMultimedia.newInstance()
                fragment.arguments = bundle
                replaceFragment(fragment, false)

            } else {
                Toast.makeText(requireContext(), "Primero guarda la nota", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        btnAtras.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    fun replaceFragment(fragment: Fragment, itstransition: Boolean) {
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()
        if (itstransition) {
            fragmentTransition.setCustomAnimations(
                android.R.anim.slide_out_right, android.R.anim.slide_in_left
            )
        }
        fragmentTransition.replace(R.id.frame_layout, fragment)
            .addToBackStack(fragment.javaClass.simpleName).commit()

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
            }
        }
        Toast.makeText(requireContext(), "Nota guardada", Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun borrarNota() {

        launch {
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAOMultimedia().borrarUnaMultimedia(noteId, tipo)
                BaseDatosNotas.getBaseDatos(it).dAOVideos().borrarUnVideo(noteId, tipo)
                BaseDatosNotas.getBaseDatos(it).dAOAudios().borrarUnAudio(noteId, tipo)
                BaseDatosNotas.getBaseDatos(it).dAONotas().borrarUnaNota(noteId)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


}

