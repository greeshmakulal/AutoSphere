package com.autosphere.dashboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_records")
data class TripRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestampMillis: Long,
    val distanceKm: Float,
    val durationMinutes: Int,
    val averageSpeedKmh: Float,
    val energyConsumedKwh: Float,
    val efficiencyWhPerKm: Int,
    val destinationName: String
)
