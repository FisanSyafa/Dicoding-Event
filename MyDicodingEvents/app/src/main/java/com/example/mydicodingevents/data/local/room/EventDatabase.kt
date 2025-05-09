package com.example.mydicodingevents.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mydicodingevents.data.local.entity.EventFavEntity

@Database(entities = [EventFavEntity::class], version = 2)
abstract class EventDatabase: RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): EventDatabase {

            @Suppress("LocalVariableName") val MIGRATION = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {

                }
            }

            if (instance == null) {
                synchronized(EventDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EventDatabase::class.java, "note_database"
                    ).addMigrations(MIGRATION)
                        .build()
                }
            }

            return instance as EventDatabase
        }

    }
}