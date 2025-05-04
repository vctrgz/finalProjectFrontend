package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CareRecord")
data class CareRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roomId: Int,
    val date: String,
    val type: String,
    val bloodPressure: String,
    val respiratoryRate: Int?,
    val pulse: Int?,
    val temperature: Float?,
    val oxygenSaturation: Int?,
    val note: String,
    val dietTexture: String? = null,
    val dietType: String? = null,
    val hygiene: String? = null,
    val sedestation: String? = null,
    val walking: String? = null,
    val posture: String? = null,
    val drainage: String? = null,
    val shiftNote: String? = null

)
