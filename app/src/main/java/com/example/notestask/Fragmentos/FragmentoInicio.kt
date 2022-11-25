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
import com.example.notestask.Entidades.Tareas
import com.example.notestask.Fragmentos.FragmentoBase
import com.example.notestask.Fragmentos.FragmentoCrearNotas
import com.example.notestask.Fragmentos.FragmentoCrearTareas
import com.example.notetask.Adaptador.AdaptadorNotas
import com.example.notetask.Adaptador.AdaptadorTareas
import kotlinx.android.synthetic.main.fragmento_inicio.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class FragmentoInicio : FragmentoBase() {

    var arrNotes = ArrayList<Notas>()
    var arrTasks= ArrayList<Tareas>()
    var adaptarNotas: AdaptadorNotas = AdaptadorNotas()
    var adaptarTareas: AdaptadorTareas= AdaptadorTareas()
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

                var tasks= BaseDatosNotas.getBaseDatos(it).dAOTareas().obtenerTodasTareas()
                adaptarTareas.setData(tasks)
                arrTasks= tasks as ArrayList<Tareas>
                recyclerViewTareas.adapter=adaptarTareas

            }
        }

        adaptarNotas.setOnClickListener(onClicked)
        adaptarTareas.setOnClickListener(onClickedT)

        fabBtnCreateNote.setOnClickListener {
            replaceFragment(FragmentoCrearNotas.newInstance(), false)
        }
        fabBtnCreateTask.setOnClickListener{
            replaceFragment(FragmentoCrearTareas.newInstance(),false)
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

                var tempArrT=ArrayList<Tareas>()
              for(arr in arrTasks){
                  if(arr.tituloT!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                      tempArrT.add(arr)
                  }
              }
                adaptarTareas.setData(tempArrT)
                adaptarTareas.notifyDataSetChanged()
                return true
            }

        })


    }


    private val onClicked = object : AdaptadorNotas.OnItemClickListener {
        override fun onClicked(notesId: Int) {


            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("noteId", notesId)
            fragment = FragmentoCrearNotas.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment, false)
        }

    }
    private val onClickedT = object : AdaptadorTareas.OnItemClickListener {
        override fun onClicked(tasksId: Int) {


            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("taskId", tasksId)
            fragment = FragmentoCrearTareas.newInstance()
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