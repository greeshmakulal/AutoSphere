package com.autosphere.dashboard.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.autosphere.dashboard.data.local.dao.TripRecordDao
import com.autosphere.dashboard.data.local.entity.TripRecordEntity

@Database(
    entities = [TripRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AutoSphereDatabase : RoomDatabase() {
    abstract fun tripRecordDao(): TripRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AutoSphereDatabase? = null

        fun getInstance(context: Context): AutoSphereDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AutoSphereDatabase::class.java,
                    "autosphere_database.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}
