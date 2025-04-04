package com.example.healhub.database

import androidx.room.*

@Dao
interface ConstanteVitalDao {
    @Query("SELECT * FROM ConstantesVitales")
    suspend fun getAll(): List<ConstanteVital>

    @Query("SELECT * FROM ConstantesVitales WHERE Cv_Paciente_Id = :pacienteId")
    suspend fun getByPacienteId(pacienteId: Int): List<ConstanteVital>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg registros: ConstanteVital)

    @Delete
    suspend fun delete(registro: ConstanteVital)
}
