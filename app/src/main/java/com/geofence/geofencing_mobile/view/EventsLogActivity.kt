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

        // Initialize the ListView and set its adapter
        listViewEventLogs = findViewById(R.id.listViewEventLogs)
        adapter = EventLogAdapter(this, eventLogs)
        listViewEventLogs.adapter = adapter

        // Initialize the controller and fetch event logs
        controller = Controller(RemoteRepository())
        fetchEventLogs()

        setOnClickListeners()
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
}
