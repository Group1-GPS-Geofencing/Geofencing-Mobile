package com.geofence.geofencing_mobile.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.geofence.geofencing_mobile.model.entities.Route

@Dao
interface RouteDao {
    @Query("SELECT * FROM route")
    suspend fun getAll(): List<Route>

    @Query("SELECT * FROM route WHERE id = :id")
    suspend fun getById(id: Long): Route?

    @Insert
    suspend fun insert(route: Route): Long

    @Update
    suspend fun update(route: Route)

    @Delete
    suspend fun delete(route: Route)
}
