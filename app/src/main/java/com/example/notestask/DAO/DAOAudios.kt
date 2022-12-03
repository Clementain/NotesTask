package com.example.notestask.DAO

import androidx.room.*
import com.example.notestask.Entidades.Audios
import com.example.notestask.Entidades.Multimedias

@Dao
interface DAOAudios {
    @Query("SELECT * FROM Audios WHERE idNota= :idN")
    suspend fun obtenerAudios(idN: Int): List<Audios>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAdios(audios: Audios)

    @Delete
    suspend fun borrarAudios(audios: Audios)

    @Query("DELETE FROM Audios WHERE idNota=:idN")
    suspend fun borrarUnAudio(idN: Int)

    @Update
    suspend fun actualizarAudio(audios: Audios)

    @Query("SELECT * FROM Audios WHERE idAud= :idA")
    suspend fun obtenerAudio(idA:Int):List<Audios>
}