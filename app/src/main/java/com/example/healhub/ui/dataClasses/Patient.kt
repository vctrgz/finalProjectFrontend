package com.example.healhub.ui.dataClasses

data class Patient(
    val numHistorial: Int,
    val diagnostico: Diagnosis,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String,
    val direccionCompleta: String,
    val lenguaMaterna: String,
    val antecedentes: String,
    val alergias: String,
    val nombreCuidador: String,
    val telefonoCuidador: String,
    val fechaIngreso: String,
    val timestamp: String,
    val registros: List<Care>
)