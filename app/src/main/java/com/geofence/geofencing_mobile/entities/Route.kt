package com.geofence.geofencing_mobile.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "routes")
data class Route(
    @PrimaryKey(autoGenerate = true)
    val routeId: Long = 0,
    val name: String,
//    val startTime: Date,
//    val endTime: Date,
    val point: String
)