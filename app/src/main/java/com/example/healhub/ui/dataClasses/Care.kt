package com.example.healhub.ui.dataClasses

data class Care(
    val registroId: Int?,
    val paciente: Patient?,
    val constantesVitales: VitalSigns,
    val numTrabajador: String,
    val fechaRegistro: String,
    val thigId: Int?,
    val observaciones: String
)
