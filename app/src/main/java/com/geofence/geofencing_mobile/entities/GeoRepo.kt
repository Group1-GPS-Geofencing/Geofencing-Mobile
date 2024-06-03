package com.geofence.geofencing_mobile.entities


import kotlinx.coroutines.flow.Flow

/**
 * author Bridget Faindani
 * Repository that provides insert, update, delete, and retrieve of entities from a given data source.
 */
interface GeoRepo {



  //methods for fence entity
    /**
     * Insert fence in the data source
     * Delete fence in the data source
     * update fence in the data source
     * and get fences in the data source
     */
    suspend fun insertFence(fence: Fence)
    fun getAllFences(): Flow<List<Fence>>
    fun getFence(id: Int): Flow<Fence>
    suspend fun updateFence(fence: Fence)
    suspend fun deleteFence(fence: Fence)

    //methods for route entity
    /**
     * Insert route in the data source
     * Delete route in the data source
     * update route in the data source
     * and get route in the data source
     */
    suspend fun insertRoute(route: Route)
    fun getAllRoutes(): Flow<List<Route>>
    suspend fun updateRoute(route: Route)
    suspend fun deleteRoute(route: Route)

    //methods for location entity
    /**
     * Insert Location in the data source
     * Delete Location in the data source
     * update Location in the data source
     * and get Location in the data source
     */
    suspend fun insertLocation(location: Location)
    fun getAllLocation(): Flow<List<Location>>
    suspend fun updateLocation(location: Location)
    suspend fun deleteLocation(location: Location)

    //methods for EventLog entity
    /**
     * Insert EventLog in the data source
     * Delete EventLog in the data source
     * update EventLog in the data source
     * and get EventLog in the data source
     */
    suspend fun insertEventLog(eventLog: EventLog)
    //suspend fun insertEventLog(eventLog: EventLog)
    fun getAllEventLog(): Flow<List<EventLog>>
    suspend fun updateEventLog(eventLog: EventLog)
    suspend fun deleteEventLog(eventLog: EventLog)


}
