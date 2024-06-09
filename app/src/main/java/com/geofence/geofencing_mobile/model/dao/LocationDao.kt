package com.geofence.geofencing_mobile.model.dao

import androidx.room.*
import com.geofence.geofencing_mobile.model.entities.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM location WHERE route_id = :routeId")
    suspend fun getByRouteId(routeId: Long): List<Location>

    @Insert
    suspend fun insert(location: Location): Long

    @Update
    suspend fun update(location: Location)

    @Delete
    suspend fun delete(location: Location)
}