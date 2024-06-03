package com.geofence.geofencing_mobile.entities

import androidx.room.Embedded
import androidx.room.Relation

data class FenceWithEventLogs(
    @Embedded val fence: Fence,
    @Relation(
        parentColumn = "id",
        entityColumn = "logId"
    )
    val eventLogs: List<EventLog>,
)
