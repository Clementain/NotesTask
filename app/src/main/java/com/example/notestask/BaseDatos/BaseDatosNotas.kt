package com.example.notestask.BaseDatos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notestask.DAO.DAONotas
import com.example.notestask.Entidades.Notas

@Database(entities = [Notas::class], version = 1, exportSchema = false)
abstract class BaseDatosNotas : RoomDatabase() {

    companion object {
        var baseDatos: BaseDatosNotas? = null

        @Synchronized
        fun getBaseDatos(context: Context): BaseDatosNotas {
            if (baseDatos == null) {
                baseDatos =
                    Room.databaseBuilder(context, BaseDatosNotas::class.java, "notes.db").build()
            }
            return baseDatos!!
        }

    }

    abstract fun dAONotas(): DAONotas
}