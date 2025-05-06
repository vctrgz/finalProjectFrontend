package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MedicalData(
    @PrimaryKey val roomId: Int,
    val hasUrinaryCatheter: Boolean,
    val hasVenousCatheter: Boolean,
    val needsOxygen: Boolean,
    val dependencyLevel: String
)
