package com.geofence.geofencing_mobile.entities

import androidx.room.Embedded
import androidx.room.Relation

data class  RouteWithLocations(
    @Embedded val route: Route,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "locationId"
    )
    val locations: List<Location>,
)
