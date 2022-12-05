package com.example.notestask.DAO

import androidx.room.*
import com.example.notestask.Entidades.Videos

@Dao
interface DAOVideos {
    @Query("SELECT * FROM videos WHERE idNota= :idN AND tipo=:t")
    suspend fun obtenerVideos(idN: Int, t: Int): List<Videos>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVideos(videos: Videos)

    @Delete
    suspend fun borrarvideos(videos: Videos)

    @Query("DELETE FROM videos WHERE idNota=:idN AND tipo=:t")
    suspend fun borrarUnVideo(idN: Int, t: Int)

    @Update
    suspend fun actualizarVideos(videos: Videos)

    @Query("SELECT * FROM videos where idV=:vId")
    suspend fun obtenervideo(vId: Int): Videos

    @Query("DELETE FROM videos where idV=:vId")
    suspend fun borrarVideo(vId: Int)

}