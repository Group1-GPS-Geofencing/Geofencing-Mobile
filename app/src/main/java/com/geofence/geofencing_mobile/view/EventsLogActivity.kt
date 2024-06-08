package com.geofence.geofencing_mobile.view

// Author: sibongire nyirenda (Bsc-com-ne-07-18)
// Last Modified by: James Kalulu (Bsc-com-ne-21-19)


import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.geofence.geofencing_mobile.R
import com.geofence.geofencing_mobile.controller.Controller
import com.geofence.geofencing_mobile.model.entities.EventLog
import com.geofence.geofencing_mobile.view.adapters.RoutesAdapter

/**
 * EventsLogActivity displays a list of event logs.
 */
class EventsLogActivity : AppCompatActivity() {

    private lateinit var listViewEventLogs: ListView
    private lateinit var adapter: RoutesAdapter
    private lateinit var controller: Controller
    private var eventLogs: MutableList<EventLog> = mutableListOf()

    /**
     * Called when the activity is first created.
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_log)
    }
}
