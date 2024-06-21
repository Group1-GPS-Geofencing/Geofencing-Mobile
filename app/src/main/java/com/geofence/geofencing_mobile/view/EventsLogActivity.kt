package com.geofence.geofencing_mobile.view

// Author: sibongire nyirenda (Bsc-com-ne-07-18)
// Last Modified by: James Kalulu (Bsc-com-ne-21-19)


import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.geofence.geofencing_mobile.R
import com.geofence.geofencing_mobile.controller.Controller
import com.geofence.geofencing_mobile.controller.RemoteRepository
import com.geofence.geofencing_mobile.model.entities.EventLog
import com.geofence.geofencing_mobile.view.adapters.EventLogAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * EventsLogActivity displays a list of event logs.
 */
class EventsLogActivity : AppCompatActivity() {

    private lateinit var listViewEventLogs: ListView
    private lateinit var adapter: EventLogAdapter
    private lateinit var controller: Controller
    private var eventLogs: MutableList<EventLog> = mutableListOf()

    /**
     * Called when the activity is first created.
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_log)
        setOnClickListeners()

        // Initialize the ListView and set its adapter
        listViewEventLogs = findViewById(R.id.listViewEventLogs)
        controller = Controller(RemoteRepository())

        adapter = EventLogAdapter(this, eventLogs) { log ->
            // Launch a coroutine to handle eventLog deletion
            CoroutineScope(Dispatchers.Main).launch {
                deleteLog(log)
            }
        }
        listViewEventLogs.adapter = adapter
        fetchEventLogs()
    }


    /**
     * Sets onClickListeners for various views in the layout.
     */
    private fun setOnClickListeners() {
        findViewById<Toolbar>(R.id.toolbarEditFence).setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * Fetches event logs from the controller and updates the adapter.
     */
    private fun fetchEventLogs() {
        CoroutineScope(Dispatchers.Main).launch {
            controller.fetchEventLogs(
                onSuccess = { logs ->
                    eventLogs.clear()
                    eventLogs.addAll(logs)
                    adapter.notifyDataSetChanged()
                },
                onError = { error ->
                    Toast.makeText(this@EventsLogActivity, "Error fetching EventLogs: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    /**
     * Deletes an eventLog from the database and updates the UI.
     * @param log The log to be deleted.
     */
    private suspend fun deleteLog(log: EventLog) {
        // Perform delete operation
        controller.deleteEventLog(log,
            onSuccess = {
                // Remove the log from the list and notify the adapter
                eventLogs.remove(log)
                adapter.notifyDataSetChanged()
                // Show success message
                Toast.makeText(this, "Log deleted successfully", Toast.LENGTH_SHORT).show()
            },
            onError = { error ->
                // Show error message if deleting route fails
                Toast.makeText(
                    this,
                    "Error deleting log: ${error.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        )
    }
}
