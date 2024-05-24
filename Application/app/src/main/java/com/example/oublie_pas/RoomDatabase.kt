package com.example.oublie_pas

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [RoomEntity::class], version = 3) // Augmentez la version de la base de données
@TypeConverters(AppDatabase.Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "room_database"
                )
                    .fallbackToDestructiveMigration() // Permettre la migration destructive
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    class Converters {
        @TypeConverter
        fun fromString(value: String): List<String> {
            return value.split(",").map { it.trim() }
        }

        @TypeConverter
        fun fromList(list: List<String>): String {
            return list.joinToString(",")
        }
    }
}
