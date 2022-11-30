package com.example.notestask.DAO

import androidx.room.*
import com.example.notestask.Entidades.Imagenes

@Dao
interface DAOImagenes {
    @Query("SELECT * FROM imagenes WHERE idNota= :idN")
    suspend fun obtenerImagenes(idN: Int): List<Imagenes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarImagen(imagenes: Imagenes)

    @Delete
    suspend fun borrarImagen(imagenes: Imagenes)

    @Query("DELETE FROM imagenes WHERE idNota=:idN")
    suspend fun borrarUnaImagen(idN: Int)

    @Update
    suspend fun actualizarImagen(imagenes: Imagenes)

    @Query("SELECT * FROM imagenes WHERE idImg= :idM")
    suspend fun obtenerImagen(idM:Int):List<Imagenes>
}