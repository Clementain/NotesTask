package com.example.notestask.DAO

import androidx.room.*
import com.example.notestask.Entidades.Audios

@Dao
interface DAOAudios {
    @Query("SELECT * FROM Audios WHERE idNota= :idN AND tipo=:t")
    suspend fun obtenerAudios(idN: Int, t: Int): List<Audios>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAdios(audios: Audios)

    @Delete
    suspend fun borrarAudios(audios: Audios)

    @Query("DELETE FROM Audios WHERE idNota=:idN AND tipo=:t")
    suspend fun borrarUnAudio(idN: Int, t: Int)

    @Update
    suspend fun actualizarAudio(audios: Audios)

}