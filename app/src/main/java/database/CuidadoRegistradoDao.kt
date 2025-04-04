package com.example.healhub.database

import androidx.room.*

@Dao
interface CuidadoRegistradoDao {
    @Query("SELECT * FROM CuidadosRegistrados")
    suspend fun getAll(): List<CuidadoRegistrado>

    @Query("SELECT * FROM CuidadosRegistrados WHERE Cr_Paciente_NumHistorial = :pacienteId")
    suspend fun getByPacienteId(pacienteId: Int): List<CuidadoRegistrado>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg registros: CuidadoRegistrado)

    @Delete
    suspend fun delete(registro: CuidadoRegistrado)
}
