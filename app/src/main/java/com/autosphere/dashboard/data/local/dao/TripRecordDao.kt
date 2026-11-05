package com.autosphere.dashboard.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.autosphere.dashboard.data.local.entity.TripRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripRecordDao {
    @Query("SELECT * FROM trip_records ORDER BY timestampMillis DESC")
    fun getAllTrips(): Flow<List<TripRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripRecordEntity): Long

    @Query("DELETE FROM trip_records")
    suspend fun clearAll()
}
