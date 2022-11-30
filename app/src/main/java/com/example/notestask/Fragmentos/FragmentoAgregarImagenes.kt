package com.example.notestask.Fragmentos

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notestask.Adaptador.ImageControler
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Imagenes
import com.example.notestask.R
import kotlinx.android.synthetic.main.f_agregar_imagenes.*
import kotlinx.coroutines.launch

class FragmentoAgregarImagenes:FragmentoBase() {
private var idN=-1
    private var urImagen:Uri?=null
    private val SELECT_ACTIVITY=50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idN=requireArguments().getInt("idN",-1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_agregar_imagenes,container,false)

    }
    companion object {
        @JvmStatic
        fun  newInstance()=FragmentoAgregarImagenes().apply {
            arguments=Bundle().apply { }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAgregarFotoCN.setOnClickListener{
            ImageControler.selectPhotoFromGallery(this, SELECT_ACTIVITY)
        }
        btnGuardarCN.setOnClickListener{
            guardarImagen()
        }
        btnAtrasCN.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
private fun guardarImagen(){
    launch {
        var imgs=Imagenes()
        imgs.uri= Uri.parse(urImagen.toString()).toString()
        imgs.tipo=tipoCN.text.toString()
        imgs.idNFK=idN
        context?.let {
            BaseDatosNotas.getBaseDatos(it).dAOImagenes().insertarImagen(imgs)
            requireActivity().supportFragmentManager.popBackStack()

        }
    }
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode==SELECT_ACTIVITY && resultCode== Activity.RESULT_OK -> {
                urImagen=data!!.data
                mostrarFotoCN.setImageURI(urImagen)
            }
        }
    }
}