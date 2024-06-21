package com.geofence.geofencing_mobile.model.entities

import kotlinx.serialization.Serializable

/**
 * Author Bridget Faindani
 * Last Modified by James Kalulu (Bsc-com-ne-21-19)
 * Entity data class represents a single entity in the database.
 * Eventlog entity
 */

@Serializable
data class EventLog(
    val id: Long = 1,
    val time: String,
    val message: String
)
