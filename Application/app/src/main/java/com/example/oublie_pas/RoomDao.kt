package com.example.oublie_pas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDao {
    @Insert
    suspend fun insert(room: RoomEntity)

    @Query("SELECT * FROM rooms")
    suspend fun getAllRooms(): List<RoomEntity>

    @Query("SELECT * FROM rooms ORDER BY id DESC LIMIT 1")
    suspend fun getLastRoom(): RoomEntity?
}
