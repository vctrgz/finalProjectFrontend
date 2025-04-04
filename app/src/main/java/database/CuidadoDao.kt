package com.example.healhub.database

import androidx.room.*

@Dao
interface CuidadoDao {
    @Query("SELECT * FROM Cuidados")
    suspend fun getAll(): List<Cuidado>

    @Query("SELECT * FROM Cuidados WHERE Cui_Id = :id")
    suspend fun getById(id: Int): Cuidado?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg cuidados: Cuidado)

    @Delete
    suspend fun delete(cuidado: Cuidado)
}
