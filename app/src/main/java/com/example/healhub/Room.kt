package com.example.healhub

import com.example.healhub.database.CareRecord

data class Room(
    val id: Int,
    val name: String,
    val patientName: String,
    val diagnosis: String,
    val lastCare: CareRecord? = null
)

