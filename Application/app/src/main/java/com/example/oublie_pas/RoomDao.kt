package com.example.oublie_pas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomDao {
    @Insert
    suspend fun insert(room: RoomEntity)

    @Query("SELECT * FROM rooms")
    suspend fun getAllRooms(): List<RoomEntity>

    @Query("SELECT * FROM rooms ORDER BY id DESC LIMIT 1")
    suspend fun getLastRoom(): RoomEntity?

    // Nouvelle fonction pour obtenir une salle par son identifiant
    @Query("SELECT * FROM rooms WHERE id = :roomId LIMIT 1")
    suspend fun getRoomById(roomId: Int): RoomEntity?

    @Update
    suspend fun updateRoom(room: RoomEntity)

    // Nouvelle fonction pour supprimer une salle par son identifiant
    @Query("DELETE FROM rooms WHERE id = :roomId")
    suspend fun deleteRoomById(roomId: Int)
}
