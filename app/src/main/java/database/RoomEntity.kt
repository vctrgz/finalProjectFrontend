package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Room")
data class RoomEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val patientName: String,
    val diagnosis: String
)
