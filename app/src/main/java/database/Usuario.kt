package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuarios")
data class Usuario(
    @PrimaryKey val Usu_Nombre: String,
    val Usu_Contrasena: String
)

@Entity(tableName = "Usuario")
data class Usuario(
    @PrimaryKey val id: Int,
    val nombre: String,
    val password: String
)