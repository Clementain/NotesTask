package com.example.notestask.DAO

import androidx.room.*
import com.example.notestask.Entidades.Multimedias

@Dao
interface DAOMultimedia {
    @Query("SELECT * FROM multimedias WHERE idNota= :idN")
    suspend fun obtenerMultimedias(idN: Int): List<Multimedias>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMultimedia(multimedias: Multimedias)

    @Delete
    suspend fun borrarMultimedia(multimedias: Multimedias)

    @Query("DELETE FROM multimedias WHERE idNota=:idN")
    suspend fun borrarUnaMultimedia(idN: Int)

    @Update
    suspend fun actualizarMultimedia(multimedias: Multimedias)

    @Query("SELECT * FROM multimedias WHERE idImg= :idM")
    suspend fun obtenerImagen(idM: Int): List<Multimedias>
}