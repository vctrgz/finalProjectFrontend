package com.example.healhub.database


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuario")
data class Usuario(
    @PrimaryKey val id: Int,
    val nombre: String,
    val password: String
)
