package com.geofence.geofencing_mobile.controller

import com.geofence.geofencing_mobile.model.entities.EventLog
import com.geofence.geofencing_mobile.model.entities.Fence
import com.geofence.geofencing_mobile.model.entities.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Controller class responsible for managing data interactions between the UI and the repository.
 * @param repository The repository responsible for handling remote data operations.
 */
class Controller(private val repository: RemoteRepository) {

    /**
     * Fetches routes from the repository.
     * @param onSuccess Callback function invoked when routes are successfully fetched.
     * @param onError Callback function invoked when an error occurs during the fetch operation.
     */
    suspend fun fetchRoutes(onSuccess: (List<Route>) -> Unit, onError: (Throwable) -> Unit) {
        try {
            repository.getRoutes(
                onSuccess = { fetchedRoutes ->
                    onSuccess(fetchedRoutes)
                },
                onError = { error ->
                    onError(error)
                }
            )
        } catch (e: Throwable) {
            onError(e)
        }
    }

    /**
     * Deletes a route from the repository.
     * @param route The route object to be deleted.
     * @param onSuccess Callback function invoked when the route is successfully deleted.
     * @param onError Callback function invoked when an error occurs during the delete operation.
     */
    suspend fun deleteRoute(route: Route, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        try {
            repository.deleteRoute(route)
            onSuccess()
        } catch (e: Throwable) {
            onError(e)
        }
    }

    /**
     * Fetches fences from the repository.
     * @param onSuccess Callback function invoked when fences are successfully fetched.
     * @param onError Callback function invoked when an error occurs during the fetch operation.
     */
    suspend fun fetchFences(onSuccess: (List<Fence>) -> Unit, onError: (Throwable) -> Unit) {
        try {
            repository.getFences(
                onSuccess = { fetchedFences ->
                    onSuccess(fetchedFences)
                },
                onError = { error ->
                    onError(error)
                }
            )
        } catch (e: Throwable) {
            onError(e)
        }
    }

    /**
     * Fetches a specific fence from the repository.
     * @param id The ID of the fence to be fetched.
     * @param onSuccess Callback function invoked when the fence is successfully fetched.
     * @param onError Callback function invoked when an error occurs during the fetch operation.
     */
    suspend fun fetchFence(id: Long, onSuccess: (Fence) -> Unit, onError: (Throwable) -> Unit) {
        try {
            val fence = repository.getFence(id)
            if (fence != null) {
                onSuccess(fence)
            }
        } catch (e: Throwable) {
            onError(e)
        }
    }

    /**
     * Creates a new fence in the repository.
     * @param fence The fence object to be created.
     * @param onSuccess Callback function invoked when the fence is successfully created, providing the ID of the created fence.
     * @param onError Callback function invoked when an error occurs during the create operation.
     */
    fun createFence(
        fence: Fence,
        onSuccess: (Long) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fenceId = repository.createFence(fence)
                withContext(Dispatchers.Main) {
                    onSuccess(fenceId)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

    /**
     * Updates a fence in the repository.
     * @param fence The updated fence object.
     * @param onSuccess Callback function invoked when the fence is successfully updated.
     * @param onError Callback function invoked when an error occurs during the update operation.
     */
    suspend fun updateFence(
        fence: Fence,
        onSuccess: (Fence) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            repository.updateFence(fence)
            onSuccess(fence)
        } catch (e: Throwable) {
            onError(e)
        }
    }

    /**
     * Deletes a fence from the repository.
     * @param fence The fence object to delete.
     * @param onSuccess Callback function invoked when the fence is successfully deleted.
     * @param onError Callback function invoked when an error occurs during the delete operation.
     */
    suspend fun deleteFence(fence: Fence, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        try {
            repository.deleteFence(fence)
            onSuccess()
        } catch (e: Throwable) {
            onError(e)
        }
    }

    /**
     * Fetches event logs from the repository.
     * @param onSuccess Callback function invoked when event logs are successfully fetched.
     * @param onError Callback function invoked when an error occurs during the fetch operation.
     */
    suspend fun fetchEventLogs(onSuccess: (List<EventLog>) -> Unit, onError: (Throwable) -> Unit) {
        try {
            val eventLogs = repository.getEventLogs()
            onSuccess(eventLogs)
        } catch (e: Throwable) {
            onError(e)
        }
    }

    /**
     * Deletes a specific event log from the repository.
     * @param eventLog The event log object to delete.
     * @param onSuccess Callback function invoked when the event log is successfully deleted.
     * @param onError Callback function invoked when an error occurs during the delete operation.
     */
    suspend fun deleteEventLog(
        eventLog: EventLog,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            repository.deleteEventLog(eventLog)
            onSuccess()
        } catch (e: Throwable) {
            onError(e)
        }
    }

}
