package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pacientes")
data class Paciente(
    @PrimaryKey val Pac_NumHistorial: Int,
    val Pac_Nombre: String,
    val Pac_Apellidos: String,
    val Pac_Fecha_Nacimiento: String, // YYYY-MM-DD
    val Pac_Direccion_Completa: String?,
    val Pac_Lengua_Materna: String = "Castellano",
    val Pac_Antecedentes: String,
    val Pac_Alergias: String,
    val Pac_Nombre_Cuidador: String?,
    val Pac_Telefono_Cuidador: String?,
    val Pac_FechaIngreso: String?, // DATETIME as String
    val Pac_Timestamp: String? // DATETIME as String
)
