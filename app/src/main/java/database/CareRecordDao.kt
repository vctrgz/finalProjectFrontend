package com.example.healhub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CareRecordDao {

    @Query("SELECT * FROM CareRecord WHERE roomId = :roomId")
    fun getByRoomId(roomId: Int): List<CareRecord>

    @Insert
    fun insert(care: CareRecord)
}
