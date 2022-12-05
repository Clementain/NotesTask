package com.example.notestask.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notestask.Adaptador.AdaptadorAudios
import com.example.notestask.Adaptador.AdaptadorImagenes
import com.example.notestask.Adaptador.AdaptadorVideos
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.R
import kotlinx.android.synthetic.main.fragmento_multimedia.*
import kotlinx.coroutines.launch

class FragmentoMultimedia : FragmentoBase() {
    var adaptadorimagen: AdaptadorImagenes = AdaptadorImagenes()
    var adaptadorvideo: AdaptadorVideos = AdaptadorVideos()
    var adaptadoraudio: AdaptadorAudios = AdaptadorAudios()

    private var idN = -1
    private var tipo = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idN = requireArguments().getInt("idN", -1)
        tipo = requireArguments().getInt("tipo", -1)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmento_multimedia, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentoMultimedia().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMultimedia.setHasFixedSize(true)
        rvMultimedia.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        rvVideos.setHasFixedSize(true)
        rvVideos.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        rvAudios.setHasFixedSize(true)
        rvAudios.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                var imgs =
                    BaseDatosNotas.getBaseDatos(it).dAOMultimedia().obtenerMultimedias(idN, tipo)
                adaptadorimagen.setData(imgs)
                rvMultimedia.adapter = adaptadorimagen

                var vids = BaseDatosNotas.getBaseDatos(it).dAOVideos().obtenerVideos(idN, tipo)
                adaptadorvideo.setData(vids)
                rvVideos.adapter = adaptadorvideo

                var auds = BaseDatosNotas.getBaseDatos(it).dAOAudios().obtenerAudios(idN, tipo)
                adaptadoraudio.setData(auds)
                rvAudios.adapter = adaptadoraudio
            }
        }

        adaptadoraudio.setOnClickListener(onClickedA)
        adaptadorimagen.setOnClickListener(onClickedM)
        adaptadorvideo.setOnClickListener(onCLickedV)

        fabBtnAddM.setOnClickListener {
            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("idN", idN)
            bundle.putInt("tipo", tipo)
            fragment = FragmentoAgregarImagenes.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment, false)

        }
        fabBtnAddMV.setOnClickListener {
            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("idNV", idN)
            bundle.putInt("tipo", tipo)
            fragment = FragmentoAgregarVideos.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment, false)
        }
        fabtnAddMA.setOnClickListener {
            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("idNA", idN)
            bundle.putInt("tipo", tipo)
            fragment = FragmentoAgregarAudio.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment, false)
        }
        btnAtrasFGM.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    private val onClickedA = object : AdaptadorAudios.OnItemClickListener {
        override fun onClicked(aId: Int) {
            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("aId", aId)
            bundle.putInt("tipo",tipo)
            fragment = FragmentoAgregarAudio.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment, false)
        }
    }

    private val onClickedM = object : AdaptadorImagenes.OnItemClickListener {
        override fun onClicked(mId: Int) {
            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("mId", mId)
            bundle.putInt("tipo",tipo)
            fragment = FragmentoAgregarImagenes.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment, false)
        }
    }

    private val onCLickedV = object : AdaptadorVideos.OnItemClickListener {
        override fun onClicked(vId: Int) {
            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("vId", vId)
            bundle.putInt("tipo",tipo)
            fragment = FragmentoAgregarVideos.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment, false)
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