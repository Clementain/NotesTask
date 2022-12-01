package com.example.notestask.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Tareas
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_crear_tareas.*
import kotlinx.android.synthetic.main.f_vista_tareas.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentoCrearTareas : FragmentoBase() {

    var currentDate: String? = null
    private var taskId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskId = requireArguments().getInt("taskId", -1)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.f_crear_tareas, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentoCrearTareas().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (taskId != -1) {

            launch {
                context?.let {
                    var tareas = BaseDatosNotas.getBaseDatos(it).dAOTareas().obtenerTarea(taskId)
                    cTituloT.setText(tareas.tituloT)
                    cDescT.setText(tareas.descripcionT)
                    // fechaCumplir.setText(tareas.fechaCumplirT)

                }
            }
        }


        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        currentDate = sdf.format(Date())

        cFechaT.text = currentDate

        btnGuardarT.setOnClickListener {
            if (taskId != -1) {
                actualizarTarea()
            } else {
                guardarTarea()
            }
        }

        btnBorrarT.setOnClickListener {
            if (taskId != -1) {
                borrarTarea()
            } else {
                Toast.makeText(requireContext(), "Primero guarda la tarea", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        btnAtrasT.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }


    private fun actualizarTarea() {
        launch {

            context?.let {
                var tareas = BaseDatosNotas.getBaseDatos(it).dAOTareas().obtenerTarea(taskId)

                tareas.tituloT = cTituloT.text.toString()
                tareas.descripcionT = cDescT.text.toString()
                tareas.fechaT = currentDate
                //   tareas.fechaCumplirT=fechaCumplir.text.toString()

                BaseDatosNotas.getBaseDatos(it).dAOTareas().actualizarTarea(tareas)
                cTituloT.setText("")
                cDescT.setText("")
                fechaCumplir.text = ""
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun guardarTarea() {

        launch {
            var tareas = Tareas()
            tareas.tituloT = cTituloT.text.toString()
            tareas.descripcionT = cDescT.text.toString()
            tareas.fechaT = currentDate
            // tareas.fechaCumplirT=fechaCumplir.text.toString()
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAOTareas().insertarTarea(tareas)
                cTituloT.setText("")
                cDescT.setText("")
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun borrarTarea() {

        launch {
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAOTareas().borrarUnaTarea(taskId)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


}

