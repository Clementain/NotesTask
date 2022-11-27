package com.example.notestask.Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Tasks")
class Tareas : Serializable {

    @PrimaryKey(autoGenerate = true)
    var idT: Int? = null

    @ColumnInfo(name = "titulo")
    var tituloT: String? = null

    @ColumnInfo(name = "fecha")
    var fechaT: String? = null

    @ColumnInfo(name = "fechaCumplir")
    var fechaCumplirT: String? = null

    @ColumnInfo(name = "descripcion")
    var descripcionT: String? = null


    override fun toString(): String {

        return "$tituloT : $fechaT"

    }
}