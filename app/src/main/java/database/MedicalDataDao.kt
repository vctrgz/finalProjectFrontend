package com.example.healhub.database

import androidx.room.*

@Dao
interface MedicalDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: MedicalData)

    @Query("SELECT * FROM MedicalData WHERE roomId = :roomId")
    suspend fun getByRoomId(roomId: Int): MedicalData?
}
