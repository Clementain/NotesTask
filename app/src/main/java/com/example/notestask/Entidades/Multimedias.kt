package com.example.notestask.Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "multimedias")
class Multimedias : Serializable {
    @PrimaryKey(autoGenerate = true)
    var idImg: Int? = null

    @ColumnInfo(name = "idNota")
    var idNFK: Int? = null

    @ColumnInfo(name = "tipo")
    var tipo: String? = null

    @ColumnInfo(name = "uri")
    var uri: String? = null


}