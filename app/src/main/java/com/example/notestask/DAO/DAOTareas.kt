package com.example.notestask.DAO

import androidx.room.*
import com.example.notestask.Entidades.Tareas

@Dao
interface DAOTareas {

    @Query("SELECT * FROM tasks ORDER BY idT DESC")
    suspend fun obtenerTodasTareas(): List<Tareas>

    @Query("SELECT * FROM tasks WHERE idT =:id")
    suspend fun obtenerTarea(id: Int): Tareas

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(task: Tareas)

    @Delete
    suspend fun borrarTarea(task: Tareas)

    @Query("DELETE FROM tasks WHERE idT =:id")
    suspend fun borrarUnaTarea(id: Int)

    @Update
    suspend fun actualizarTarea(task: Tareas)



}