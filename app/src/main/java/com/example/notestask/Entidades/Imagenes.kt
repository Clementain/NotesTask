package com.example.notestask.Entidades

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "imagenes")
class Imagenes : Serializable {
    @PrimaryKey(autoGenerate = true)
    var idImg: Int? = null

    @ColumnInfo(name = "idNota")
    var idNFK: Int? = null

    @ColumnInfo(name = "tipo")
    var tipo: String? = null

    @ColumnInfo(name = "uri")
    var uri: Uri? = null


}