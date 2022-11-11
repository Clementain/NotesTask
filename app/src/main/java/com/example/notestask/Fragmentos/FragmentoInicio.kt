package com.example.notestask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notestask.BaseDatos.BaseDatosNotas
import com.example.notestask.Entidades.Notas
import com.example.notestask.Fragmentos.FragmentoBase
import com.example.notestask.Fragmentos.Fragmentocrearnotas
import com.example.notetask.Adaptador.AdaptadorNotas
import kotlinx.android.synthetic.main.fragmento_inicio.*
import kotlinx.coroutines.launch
import java.util.*

class FragmentoInicio : FragmentoBase() {

    var arrNotes = ArrayList<Notas>()
    var adaptarNotas: AdaptadorNotas = AdaptadorNotas()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmento_inicio, container, false)

    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentoInicio().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                var notes = BaseDatosNotas.getBaseDatos(it).dAONotas().obtenerTodasNotas()
                adaptarNotas.setData(notes)
                arrNotes = notes as ArrayList<Notas>
                recyclerView.adapter = adaptarNotas
            }
        }

        adaptarNotas.setOnClickListener(onClicked)

        fabBtnCreateNote.setOnClickListener {
            replaceFragment(Fragmentocrearnotas.newInstance(), false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var tempArr = ArrayList<Notas>()

                for (arr in arrNotes) {
                    if (arr.titulo!!.toLowerCase(Locale.getDefault()).contains(p0.toString())) {
                        tempArr.add(arr)
                    }
                }

                adaptarNotas.setData(tempArr)
                adaptarNotas.notifyDataSetChanged()
                return true
            }

        })


    }


    private val onClicked = object : AdaptadorNotas.OnItemClickListener {
        override fun onClicked(notesId: Int) {


            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("noteId", notesId)
            fragment = Fragmentocrearnotas.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment, false)
        }

    }


    fun replaceFragment(fragment: Fragment, istransition: Boolean) {
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if (istransition) {
            fragmentTransition.setCustomAnimations(
                android.R.anim.slide_out_right, android.R.anim.slide_in_left
            )
        }
        fragmentTransition.replace(R.id.frame_layout, fragment)
            .addToBackStack(fragment.javaClass.simpleName).commit()
    }


}