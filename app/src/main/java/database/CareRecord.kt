package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "care_records")
data class CareRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roomId: Int,
    val date: String,
    val type: String,
    val bloodPressure: String,
    val note: String
)




