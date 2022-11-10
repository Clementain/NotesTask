package com.example.notestask.Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Notes")
class Notas : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "titulo")
    var titulo: String? = null

    @ColumnInfo(name = "fecha")
    var fecha: String? = null

    @ColumnInfo(name = "tarea")
    var tarea: Boolean? = null

    @ColumnInfo(name = "descripcion")
    var descripcion: String? = null

    @ColumnInfo(name = "imagen")
    var imagen: String? = null

    @ColumnInfo(name = "video")
    var video: String? = null

    @ColumnInfo(name = "audio")
    var audio: String? = null


    override fun toString(): String {

        return "$titulo : $fecha"

    }
}