package com.geofence.geofencing_mobile.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "locations")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val locationId: Long = 0,
    val point: String,
//    val time: Timestamp
)