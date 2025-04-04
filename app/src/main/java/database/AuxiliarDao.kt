package com.example.healhub.database

import androidx.room.*

@Dao
interface AuxiliarDao {
    @Query("SELECT * FROM Auxiliares")
    suspend fun getAll(): List<Auxiliar>

    @Query("SELECT * FROM Auxiliares WHERE Aux_NumTrabajador = :id")
    suspend fun getById(id: String): Auxiliar?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg auxiliares: Auxiliar)

    @Delete
    suspend fun delete(auxiliar: Auxiliar)
}
