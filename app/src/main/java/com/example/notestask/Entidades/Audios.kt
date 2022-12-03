package com.example.notestask.Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Audios")
class Audios : Serializable {
    @PrimaryKey(autoGenerate = true)
    var idAud: Int? = null

    @ColumnInfo(name = "idNota")
    var idNFK: Int? = null

    @ColumnInfo(name = "tipo")
    var tipo: String? = null

    @ColumnInfo(name = "uri")
    var uri: String? = null


}