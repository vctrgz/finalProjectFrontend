package com.example.healhub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.healhub.database.RoomEntity
import com.example.healhub.database.RoomDao
import com.example.healhub.database.Habitacion
import com.example.healhub.database.HabitacionDao
import com.example.healhub.database.Paciente
import com.example.healhub.database.PacienteDao
import com.example.healhub.database.Cuidado
import com.example.healhub.database.CuidadoDao
import com.example.healhub.database.CuidadoRegistrado
import com.example.healhub.database.CuidadoRegistradoDao
import com.example.healhub.database.ConstanteVital
import com.example.healhub.database.ConstanteVitalDao
import com.example.healhub.database.Auxiliar
import com.example.healhub.database.AuxiliarDao
import com.example.healhub.database.CareRecord
import com.example.healhub.database.CareRecordDao
import com.example.healhub.database.Usuario

@Database(
    entities = [
        RoomEntity::class,
        CareRecord::class,
        Usuario::class,
        Habitacion::class,
        Paciente::class,
        Cuidado::class,
        CuidadoRegistrado::class,
        ConstanteVital::class,
        Auxiliar::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun habitacionDao(): HabitacionDao
    abstract fun pacienteDao(): PacienteDao
    abstract fun cuidadoDao(): CuidadoDao
    abstract fun cuidadoRegistradoDao(): CuidadoRegistradoDao
    abstract fun constanteVitalDao(): ConstanteVitalDao
    abstract fun auxiliarDao(): AuxiliarDao
    abstract fun careRecordDao(): CareRecordDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun roomDao(): RoomDao


    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "healhub_db"
                ).fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}