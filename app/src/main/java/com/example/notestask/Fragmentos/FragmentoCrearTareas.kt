package com.example.notestask.Fragmentos

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.notestask.Alarmas.*
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Tareas
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_crear_tareas.*
import kotlinx.android.synthetic.main.f_vista_tareas.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class FragmentoCrearTareas : FragmentoBase() {

    var currentDate: String? = null
    var lastDate: String? = null
    var diferencia: Long? = null
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

    @RequiresApi(Build.VERSION_CODES.O)
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


        val sdf = SimpleDateFormat("d/MM/yyyy h:m")

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
            lastDate = FechaCumplir.text.toString() + " " + horaCumplir.text.toString()
            val dS = sdf.parse(currentDate.toString())
            val dE = sdf.parse(lastDate.toString())
            diferenciaFechas(dS as Date, dE as Date)
            programarNotificacion(cTituloT.text.toString())
            crearCanal()
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
        btnCalendario.setOnClickListener {
            mostrarCalendario()
        }
        btnReloj.setOnClickListener {
            mostrarReloj()
        }

    }

    private fun diferenciaFechas(Fi: Date, Ff: Date) {
        diferencia = abs(Ff.time - Fi.time)
    }

    private fun mostrarCalendario() {
        val newFragment = Calendario { day, month, year -> onDateSelected(day, month, year) }
        activity?.let { newFragment.show(it.supportFragmentManager, "calendario") }
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        FechaCumplir.text = "$day/$month/$year"
    }

    private fun mostrarReloj() {
        val newFragment = Reloj { onTimeSelected(it) }
        activity?.let { newFragment.show(it.supportFragmentManager, "reloj") }
    }

    private fun onTimeSelected(time: String) {
        horaCumplir.text = time
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearCanal() {
        val name = "Tareas Channel"
        val desc = "Descripcion Canal Tareas"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun programarNotificacion(titulo: String) {
        val intent = Intent(context, AlarmaReceiver::class.java)
        val message = "No se te olvide completar esta tarea"
        intent.putExtra(titleExtra, titulo)
        intent.putExtra(messageExtra, message)

        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            context, notificationID, intent, 0
        )
        // val time = getTime()
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            //time,
            SystemClock.elapsedRealtime() + diferencia!!, pendingIntent
        )
        AlarmaReceiver().onReceive(requireActivity().applicationContext, intent)
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

}

