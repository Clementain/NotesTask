package com.example.notestask.Fragmentos

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Notas
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_crear_notas.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*

class Fragmentocrearnotas : FragmentoBase(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    var fechaActual: String? = null
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE = 456
    private var selectedImagePath = ""
    private var webLink = ""
    private var idNota = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idNota = requireArguments().getInt("idNota", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_crear_notas, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = Fragmentocrearnotas().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (idNota != -1) {
            launch {
                context?.let {
                    var notas = BaseDatosNotas.getBaseDatos(it).dAONotas().obtenerNota(idNota)
                    cTitulo.setText(notas.titulo)
                    cDesc.setText(notas.descripcion)
                }
            }
        }
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        fechaActual = sdf.format(Date())
        cFecha.text = fechaActual

        btnGuardar.setOnClickListener {
            if (idNota != -1) {
                actualizarNota()
            } else {
                guardarNota()
            }
        }

        btnAtras.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun actualizarNota() {
        launch {
            context?.let {
                var notas = BaseDatosNotas.getBaseDatos(it).dAONotas().obtenerNota(idNota)
                notas.titulo = cTitulo.text.toString()
                notas.descripcion = cDesc.text.toString()
                notas.fecha = fechaActual

                BaseDatosNotas.getBaseDatos(it).dAONotas().actualizarNota(notas)
                cTitulo.setText("")
                cDesc.setText("")
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun guardarNota() {
        launch {
            var notas = Notas()
            notas.titulo = cTitulo.text.toString()
            notas.descripcion = cDesc.text.toString()
            notas.fecha = fechaActual

            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAONotas().insertarNota(notas)
                cTitulo.setText("")
                cDesc.setText("")
                requireActivity().supportFragmentManager.popBackStack()

            }
        }
    }

    private fun borrarNota() {
        launch {
            context?.let {
                BaseDatosNotas.getBaseDatos(it).dAONotas().borrarNota(idNota)
                requireActivity().supportFragmentManager.popBackStack()

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

    private val BroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var actionColor = p1!!.getStringExtra("action")
            when (actionColor!!) {
                "DeleteNote" -> {
                    borrarNota()
                }
            }

        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()
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

