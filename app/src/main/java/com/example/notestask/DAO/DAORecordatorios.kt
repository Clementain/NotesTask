package com.example.notestask.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.notestask.Entidades.Reminder

@Dao
interface DAORecordatorios {
    @Insert
    fun insert(reminder: Reminder)
    @Query("SELECT * FROM Reminder WHERE noteID=:id")
    fun getAllReminders(id: Int): MutableList<Reminder>

    @Query("DELETE FROM Reminder WHERE noteID = :id")
    fun deleteAllReminders(id: Int)

    @Delete
    fun deleteReminder(reminder:Reminder)

    @Query("SELECT MAX(ID) FROM reminder")
    fun getMaxId():Int

}