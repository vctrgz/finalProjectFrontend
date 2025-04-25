package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CuidadosRegistrados")
data class CuidadoRegistrado(
    @PrimaryKey val Cr_Id: Int,
    val Cr_Cui_Id: Int, // 外键 -> Cuidado
    val Cr_Paciente_NumHistorial: Int,
    val Cr_RealizadoPor_Aux: String,
    val Cr_Fecha: String, // DATETIME
    val Cr_Observaciones: String?
)