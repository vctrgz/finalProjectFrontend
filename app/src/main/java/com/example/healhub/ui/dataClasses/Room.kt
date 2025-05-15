package com.example.healhub.ui.dataClasses

import com.example.healhub.ui.dataClasses.Patient


data class Room(
    val habitacionId: Int,
    val observaciones: String,
    val paciente: Patient
    //val diagnosis: String,
    //val lastCare: CareRecord? = null
)

