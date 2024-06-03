package com.geofence.geofencing_mobile.entities


import kotlinx.coroutines.flow.Flow

class OfflineGeoRepo(private val geoDao: GeoDao) : GeoRepo {
//    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()
//
//    override fun getItemStream(id: Int): Flow<Item?> = itemDao.getItem(id)

    override suspend fun insertFence(fence: Fence) = geoDao.insert(fence)
    override fun getAllFences(): Flow<List<Fence>> = geoDao.getAllFences()

    override fun getFence(id: Int): Flow<Fence> = geoDao.getFence(id)

    override suspend fun updateFence(fence: Fence) = geoDao.updateFence(fence)

    override suspend fun deleteFence(fence: Fence) = geoDao.deleteFence(fence)
//route
    override suspend fun insertRoute(route: Route) = geoDao.insert(route)
    override fun getAllRoutes(): Flow<List<Route>> = geoDao.getAllRoutes()

    override suspend fun updateRoute(route: Route)  = geoDao.updateRoute(route)

    override suspend fun deleteRoute(route: Route) = geoDao.deleteRoute(route)


    //locations
    override suspend fun insertLocation(location: Location) = geoDao.insert(location)
    override fun getAllLocation(): Flow<List<Location>> = geoDao.getAllLocations()

    override suspend fun updateLocation(location: Location)  = geoDao.updateLocation(location)

    override suspend fun deleteLocation(location: Location) = geoDao.deleteLocation(location)

//EventLog
    override suspend fun insertEventLog(eventLog: EventLog) =geoDao.insert(eventLog)
    override fun getAllEventLog(): Flow<List<EventLog>> = geoDao.getAllEventLogs()
    override suspend fun updateEventLog(eventLog: EventLog)  = geoDao.updateEventLog(eventLog)

    override suspend fun deleteEventLog(eventLog: EventLog) = geoDao.deleteEventLog(eventLog)
//    override suspend fun deleteItem(item: Item) = itemDao.delete(item)
//
//    override suspend fun updateItem(item: Item) = itemDao.update(item)
}