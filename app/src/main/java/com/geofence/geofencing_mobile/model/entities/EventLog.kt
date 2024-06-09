package com.geofence.geofencing_mobile.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Author Bridget Faindani
 * Entity data class represents a single entity in the database.
 * Eventlog entity
 */

@Serializable
@Entity(
    tableName = "event_log",
    foreignKeys = [ForeignKey(
        entity = Fence::class,
        parentColumns = ["id"],
        childColumns = ["fence_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EventLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1,
    @ColumnInfo(name = "fence_id")
    val fenceId: Long,
    val time: String,
    val message: String
)
