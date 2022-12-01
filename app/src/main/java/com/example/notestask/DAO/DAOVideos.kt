package com.example.notestask.DAO

import androidx.room.*
import com.example.notestask.Entidades.Multimedias
import com.example.notestask.Entidades.Videos

@Dao
interface DAOVideos {
    @Query("SELECT * FROM videos WHERE idNota= :idN")
    suspend fun obtenerVideos(idN: Int): List<Videos>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVideos(videos: Videos)

    @Delete
    suspend fun borrarvideos(videos: Videos)

    @Query("DELETE FROM videos WHERE idNota=:idN")
    suspend fun borrarUnVideo(idN: Int)

    @Update
    suspend fun actualizarVideos(videos: Videos)

    @Query("SELECT * FROM videos WHERE idV= :idV")
    suspend fun obtenerImagen(idV:Int):List<Videos>
}