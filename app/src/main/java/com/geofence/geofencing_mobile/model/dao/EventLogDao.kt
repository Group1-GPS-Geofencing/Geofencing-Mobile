package com.geofence.geofencing_mobile.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.geofence.geofencing_mobile.model.entities.EventLog

/**
 * author Bridget faiandani
 * query event  by fence id fence in the data source
 * insert, update, delete and query all fences

 */
@Dao
interface EventLogDao {
    @Query("SELECT * FROM event_log WHERE fence_id = :fenceId")
    suspend fun getByFenceId(fenceId: Long): List<EventLog>

    @Insert
    suspend fun insert(eventLog: EventLog): Long

    @Update
    suspend fun update(eventLog: EventLog)

    @Delete
    suspend fun delete(eventLog: EventLog)

    @Query("SELECT * FROM event_log")
    suspend fun getAll(): List<EventLog>
}
