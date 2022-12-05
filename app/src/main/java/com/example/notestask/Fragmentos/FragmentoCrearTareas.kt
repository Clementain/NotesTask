package com.example.notestask.Fragmentos

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Tareas
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_crear_tareas.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentoCrearTareas : FragmentoBase() {

    var currentDate: String? = null
    private var dia = 0
    private var mes = 0
    private var anio = 0
    private var hora = 0
    private var minutos = 0
    private var taskId = -1
    private var tipo = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskId = requireArguments().getInt("taskId", -1)
        tipo = 2

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

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (taskId != -1) {

            launch {
                context?.let {
                    var tareas = BaseDatosNotas.getBaseDatos(it).dAOTareas().obtenerTarea(taskId)
                    cTituloT.setText(tareas.tituloT)
                    cDescT.setText(tareas.descripcionT)
                    FechaCumplir.text = tareas.fechaCumplirT
                    horaCumplir.text = tareas.horaCumplirT
                }
            }
        }


        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm")

        currentDate = sdf.format(Date())

        cFechaT.text = currentDate

        btnGuardarT.setOnClickListener {
            if (taskId != -1) {
                actualizarTarea()
            } else {
                guardarTarea()
            }
        }
        btnFechaRecordar.setOnClickListener {
            programarNotificacion(cTituloT.text.toString())
        }
        btnBorrarT.setOnClickListener {
            if (taskId != -1) {
                borrarTarea()
            } else {
                Toast.makeText(requireContext(), "Primero guarda la tarea", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        btnAgregarMultimediaT.setOnClickListener {
          var  fragment: Fragment
          var bundle= Bundle()
            if (taskId != -1) {
                bundle.putInt("idN", taskId)

            } else {
                launch {
                    context?.let {
                        val id = BaseDatosNotas.getBaseDatos(it).dAOTareas().obtenerId()
                        if (id == null) {
                            bundle.putInt("idN", 1)
                        } else {
                            bundle.putInt("idN", id + 1)
                        }

                    }
                }
            }
            bundle.putInt("tipo",tipo)
            fragment = FragmentoMultimedia.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment, false)
        }

        btnAtrasT.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        btnCalendario.setOnClickListener {
            mostrarCalendario()
        }
        btnReloj.setOnClickListener {
            mostrarReloj()
        }

    }


    private fun mostrarCalendario() {
        val newFragment = Calendario { day, month, year -> onDateSelected(day, month, year) }
        activity?.let { newFragment.show(it.supportFragmentManager, "calendario") }
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val mescorregido = month + 1
        FechaCumplir.text = "$day/$mescorregido/$year"
        this.anio = year
        this.dia = day
        this.mes = month

    }

    private fun mostrarReloj() {
        val newFragment = Reloj { hour, minute -> onTimeSelected(hour, minute) }
        activity?.let { newFragment.show(it.supportFragmentManager, "reloj") }
    }

    private fun onTimeSelected(hour: Int, minute: Int) {
        horaCumplir.text = "$hour:$minute"
        this.hora = hour
        this.minutos = minute
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun programarNotificacion(titulo: String) {
        getTime(titulo)
    }

    private fun startAlarm(calendar: Calendar, titulo: String) {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmaReceiver::class.java)
        val message = "Tienes esta tarea pendiente"
        intent.putExtra(tituloExtra2, titulo)
        intent.putExtra(mensajeExtra2, message)
        val pendingIntent = PendingIntent.getBroadcast(context, notificationID, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun getTime(titulo: String) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hora)
        calendar.set(Calendar.MINUTE, minutos)
        calendar.set(Calendar.SECOND, 0)
        startAlarm(calendar, titulo)
    }

    private fun actualizarTarea() {
        launch {

            context?.let {
                var tareas = BaseDatosNotas.getBaseDatos(it).dAOTareas().obtenerTarea(taskId)

                tareas.tituloT = cTituloT.text.toString()
                tareas.descripcionT = cDescT.text.toString()
                tareas.fechaT = currentDate
                tareas.fechaCumplirT = FechaCumplir.text.toString()
                tareas.horaCumplirT = horaCumplir.text.toString()

                BaseDatosNotas.getBaseDatos(it).dAOTareas().actualizarTarea(tareas)
                cTituloT.setText("")
                cDescT.setText("")
                FechaCumplir.text = ""
                horaCumplir.text = ""
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
            tareas.fechaCumplirT = FechaCumplir.text.toString()
            tareas.horaCumplirT = horaCumplir.text.toString()
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAOTareas().insertarTarea(tareas)
                cTituloT.setText("")
                cDescT.setText("")
                FechaCumplir.text = ""
                horaCumplir.text = ""
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

}

