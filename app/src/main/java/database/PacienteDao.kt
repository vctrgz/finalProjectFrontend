package com.example.healhub.database

import androidx.room.*

@Dao
interface PacienteDao {
    @Query("SELECT * FROM Pacientes")
    suspend fun getAll(): List<Paciente>

    @Query("SELECT * FROM Pacientes WHERE Pac_NumHistorial = :id")
    suspend fun getById(id: Int): Paciente?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg pacientes: Paciente)

    @Delete
    suspend fun delete(paciente: Paciente)
}
