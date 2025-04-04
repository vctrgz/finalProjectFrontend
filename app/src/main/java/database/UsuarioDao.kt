package com.example.healhub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import database.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM Usuario WHERE nombre = :name AND password = :pass")
    suspend fun login(name: String, pass: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: Usuario)
}
