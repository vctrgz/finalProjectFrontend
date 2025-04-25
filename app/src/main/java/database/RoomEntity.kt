package com.example.healhub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Room")
data class RoomEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val patientName: String,
    val diagnosis: String,
    val observaciones: String? = null  // 新增字段，用于存储观察记录
)
