package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Auxiliares")
data class Auxiliar(
    @PrimaryKey val Aux_NumTrabajador: String,
    val Aux_Nombre: String,
    val Aux_Apellidos: String
)