package com.example.oublie_pas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titre: String,
    val description: String,
    val toggleRappel: Boolean,
    val dateInMillis: Long,
    val status: String,
    val tags: List<String>
)
