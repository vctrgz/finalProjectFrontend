package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Habitaciones")
data class Habitacion(
    @PrimaryKey val Hab_Id: Int,
    val Hab_Nombre: String,
    val Hab_Paciente_NumHistorial: Int? // Nullable 外键
)