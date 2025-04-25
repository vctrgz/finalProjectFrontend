package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cuidados")
data class Cuidado(
    @PrimaryKey val Cui_Id: Int,
    val Cui_Tipo: String,
    val Cui_Descripcion: String
)