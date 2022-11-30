package com.example.notestask.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notestask.Adaptador.AdaptadorImagenes
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Imagenes
import com.example.notestask.R
import kotlinx.android.synthetic.main.fragmento_multimedia.*
import kotlinx.coroutines.launch

class FragmentoMultimedia : FragmentoBase() {
    var arrlistImagenes = ArrayList<Imagenes>()
    var adaptadorimagen: AdaptadorImagenes = AdaptadorImagenes()
    private var idN = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idN = requireArguments().getInt("idN", -1)
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
        launch {
            context?.let {
                var imgs = BaseDatosNotas.getBaseDatos(it).dAOImagenes().obtenerImagenes(idN)
                adaptadorimagen.setData(imgs)
                arrlistImagenes = imgs as ArrayList<Imagenes>
                rvMultimedia.adapter = adaptadorimagen

            }
        }
        fabBtnAddM.setOnClickListener {
            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("idN", idN)
            fragment = FragmentoAgregarImagenes.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment, false)

        }
    }


    fun replaceFragment(fragment: Fragment, itstransition: Boolean) {
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()
        if (itstransition) {
            fragmentTransition.setCustomAnimations(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
        }
        fragmentTransition.replace(R.id.frame_layout, fragment)
            .addToBackStack(fragment.javaClass.simpleName).commit()

    }
}