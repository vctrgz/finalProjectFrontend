package com.example.healhub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDao {

    @Query("SELECT * FROM Room")
    fun getAllRooms(): List<RoomEntity>

    @Query("SELECT * FROM Room WHERE id = :roomId")
    fun getRoomById(roomId: Int): RoomEntity?

    @Insert
    fun insertRoom(room: RoomEntity)
}
