package com.geofence.geofencing_mobile.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "event_logs")
data class EventLog(
    @PrimaryKey(autoGenerate = true)
    val logId: Int,
    val message: String,
    @TypeConverters(DateConverter::class) val timestamp: java.util.Date
)
