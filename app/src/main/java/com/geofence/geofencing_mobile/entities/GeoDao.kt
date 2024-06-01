package com.geofence.geofencing_mobile.entities


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface GeoDao {


//

    // fence
    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fence: Fence)

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * from fences ORDER BY id ASC")
    fun getAllFences(): Flow<List<Fence>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * from fences WHERE id = :id")
    fun getFence(id: Int): Flow<Fence>

    @Update
    suspend fun updateFence(fence: Fence)

    @Delete
    suspend fun deleteFence(fence: Fence)

    // Routes
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(route: Route)

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * from routes ORDER BY routeId ASC")
    fun getAllRoutes(): Flow<List<Route>>

    @Update
    suspend fun updateRoute(route: Route)

    @Delete
    suspend fun deleteRoute(route: Route)

    //Location
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location)

    @Query("SELECT * from locations ORDER BY locationId ASC")
    fun getAllLocations(): Flow<List<Location>>

    @Update
    suspend fun updateLocation(location: Location)

    @Delete
    suspend fun deleteLocation(location: Location)

    //Event Logs
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(eventLog: EventLog)

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * from event_logs ")
    fun getAllEventLogs(): Flow<List<EventLog>>

    //@Query("SELECT * FROM event_logs WHERE timestamp >= :startTime AND timestamp <= :endTime")
    //suspend fun getEventLogsInTimeRange(startTime: Long, endTime: Long): List<EventLog>

    @Update
    suspend fun updateEventLog(eventLog: EventLog)

    @Delete
    suspend fun deleteEventLog(eventLog: EventLog)

    // Fence with EventLogs
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM fences")
    fun getFencesWithEventLogs(): Flow<List<FenceWithEventLogs>>

    //RouteWithLocations
    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM routes")
    fun getRouteWithLocations(): Flow<List<RouteWithLocations>>






}
