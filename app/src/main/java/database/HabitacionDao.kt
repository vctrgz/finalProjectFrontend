package com.example.healhub.database

import androidx.room.*
import com.example.healhub.database.Habitacion

@Dao
interface HabitacionDao {
    @Query("SELECT * FROM Habitaciones")
    suspend fun getAll(): List<Habitacion>

    @Query("SELECT * FROM Habitaciones WHERE Hab_Id = :id")
    suspend fun getById(id: Int): Habitacion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg habitaciones: Habitacion)

    @Delete
    suspend fun delete(habitacion: Habitacion)
}
