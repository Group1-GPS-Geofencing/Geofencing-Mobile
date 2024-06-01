package com.geofence.geofencing_mobile.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "fences")
data class Fence(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val fenceName: String,
    val description: String,
    val boundary: Int,
    val isActive: Boolean,
)