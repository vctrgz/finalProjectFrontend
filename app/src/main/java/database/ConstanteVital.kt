package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ConstantesVitales")
data class ConstanteVital(
    @PrimaryKey val Cv_Id: Int,
    val Cv_Paciente_Id: Int, // 外键 - Pac_NumHistorial
    val Cv_TA_Sistolica: Double?,
    val Cv_TA_Diastolica: Double?,
    val Cv_FrecuenciaCardiaca: Double?,
    val Cv_FrecuenciaRespiratoria: Double?,
    val Cv_Temperatura: Double?,
    val Cv_SaturacionOxigeno: Double?,
    val Cv_GlucemiaCapilar: Double?,
    val Cv_Peso: Double?,
    val Cv_Talla: Double?,
    val Cv_PerimetroCefalico: Double?,
    val Cv_Diuresis: Double?,
    val Cv_Heces: Double?,
    val Cv_Vomitos: Double?,
    val Cv_Observaciones: String?,
    val Cv_Timestamp: String? // DATETIME
)