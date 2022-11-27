package com.example.notestask.DAO

import androidx.room.*
import com.example.notestask.Entidades.Notas

@Dao
interface DAONotas {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    suspend fun obtenerTodasNotas(): List<Notas>

    @Query("SELECT * FROM notes WHERE id =:id")
    suspend fun obtenerNota(id: Int): Notas

    @Query("SELECT MAX(id) FROM NOTES")
    suspend fun obtenerId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(note: Notas)

    @Delete
    suspend fun borrarNota(note: Notas)

    @Query("DELETE FROM notes WHERE id =:id")
    suspend fun borrarUnaNota(id: Int)

    @Update
    suspend fun actualizarNota(note: Notas)


}