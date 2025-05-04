package com.example.healhub.database

import androidx.room.*

@Dao
interface PatientDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: PatientData)

    @Query("SELECT * FROM PatientData WHERE roomId = :roomId")
    suspend fun getByRoomId(roomId: Int): PatientData?
}
