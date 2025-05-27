package com.example.healhub.ui.dataClasses

data class Diagnosis(
    val diaId: Int,
    //val registro: Care,
    val diaDiagnostico: String,
    val diaMotivo: String,
    val diaO2: Int,
    val diaO2Desc: String,
    val diaPanales: Int,
    val diaDesc: String,
    val diaSV: Int,
    val diaSVTipo: String,
    val diaSVDebito: String,
    val diaSR: Int,
    val diaSRDebito: String,
    val diaSNG: Int,
    val diaSNGDesc: String
)
