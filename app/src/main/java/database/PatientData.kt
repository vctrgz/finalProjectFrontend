package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PatientData(
    @PrimaryKey val roomId: Int,
    val fullName: String,
    val birthDate: String,
    val language: String,
    val address: String
)

