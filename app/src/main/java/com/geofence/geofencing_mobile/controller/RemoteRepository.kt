package com.geofence.geofencing_mobile.controller;

import android.util.Log
import com.geofence.geofencing_mobile.model.entities.EventLog
import com.geofence.geofencing_mobile.model.entities.Fence
import com.geofence.geofencing_mobile.model.entities.Route
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * RemoteRepository is responsible for handling communication with the remote server.
 * @property TAG A tag used for logging.
 * @property urlString The base URL of the remote server.
 */
public class RemoteRepository {
    // Logging
    private val TAG = "Remote Repository"

    // Server URL
    private val urlString = "https://geofencing-backend.onrender.com/api/v1"
    //  private val urlString = "http://192.168.42.55:8080/api/v1"

    // JSON serializer
    private val json = Json { ignoreUnknownKeys = true }

    // HTTP client with JSON support
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                allowStructuredMapKeys = true
                encodeDefaults = true
            })
        }
    }

    /**
     * Retrieves a list of all fences from the server.
     * @param onSuccess Callback function invoked when the operation is successful.
     * @param onError Callback function invoked when an error occurs.
     */
    suspend fun getFences(
        onSuccess: (List<Fence>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            val fencesJsonString: String = client.get("$urlString/fence").body()
            val fences: List<Fence> = json.decodeFromString(fencesJsonString)
            onSuccess(fences)
        } catch (e: Throwable) {
            onError(e)
        }
    }

    /**
     * Retrieves a specific fence from the server.
     * @param id The ID of the fence to retrieve.
     * @return The retrieved fence, or null if not found.
     */
    suspend fun getFence(id: Long): Fence? {
        Log.d(TAG, "Fetching fence with id $id from the server")
        return client.get("$urlString/fence/$id").body()
    }

    /**
     * Creates a new fence on the server.
     * @param fence The fence object to create.
     * @return The ID of the created fence, or -1 if creation fails.
     */
    suspend fun createFence(fence: Fence): Long {
        val response: Fence = client.post("$urlString/fence") {
            contentType(ContentType.Application.Json)
            setBody(fence)
        }.body()
        return response.id ?: -1L // Ensure to return a valid ID or handle null case
    }

    /**
     * Updates a fence on the server.
     * @param fence The fence object with updated details.
     */
    suspend fun updateFence(fence: Fence) {
        Log.d(TAG, "Updating fence with id ${fence.id} on the server")
        client.put("$urlString/fence/${fence.id}") {
            contentType(ContentType.Application.Json)
            setBody(fence)
        }
    }

    /**
     * Deletes a fence from the server.
     * @param fence The fence object to delete.
     */
    suspend fun deleteFence(fence: Fence) {
        Log.d(TAG, "Deleting fence with id ${fence.id} from the server")
        client.delete("$urlString/fence/${fence.id}") {
            contentType(ContentType.Application.Json)
            setBody(fence)
        }
    }


    /**
     * Retrieves a list of all event logs from the server.
     * @return A list of event logs fetched from the server.
     */
    suspend fun getEventLogs(): List<EventLog> {
        Log.d(TAG, "Fetching event logs from the server")
        return client.get("$urlString/logs").body()
    }

    /**
     * Deletes a specific event log from the server.
     * @param eventLog The event log object to delete.
     */
    suspend fun deleteEventLog(eventLog: EventLog) {
        Log.d(TAG, "Deleting event log with id ${eventLog.id} from the server")
        client.delete("$urlString/logs/${eventLog.id}")
    }

    /**
     * Retrieves a list of all routes from the server.
     * @param onSuccess Callback function invoked when the operation is successful.
     * @param onError Callback function invoked when an error occurs.
     */
    suspend fun getRoutes(
        onSuccess: (List<Route>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            val routesJsonString: String = client.get("$urlString/route").body()
            val routes: List<Route> = json.decodeFromString(routesJsonString)
            onSuccess(routes)
        } catch (e: Throwable) {
            onError(e)
        }
    }

    /**
     * Retrieves a specific route from the server.
     * @param id The ID of the route to retrieve.
     * @return The retrieved route, or null if not found.
     */
    suspend fun getRoute(id: Long): Route? {
        Log.d(TAG, "Fetching fence with id $id from the server")
        return client.get("$urlString/route/$id").body()
    }

    /**
     * Deletes a route from the server.
     * @param route The route object to delete.
     */    suspend fun deleteRoute(route: Route) {
        Log.d(TAG, "Deleting fence with id ${route.id} from the server")
        client.delete("$urlString/route/${route.id}")
    }

}
