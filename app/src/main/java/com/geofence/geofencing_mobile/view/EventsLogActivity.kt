package com.geofence.geofencing_mobile.view

// Author: sibongire nyirenda (Bsc-com-ne-07-18)
// Last Modified on: 2-06-2024


// Import necessary Android libraries.
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geofence.geofencing_mobile.R

// EventsLogActivity is a screen within the application that displays logs related to events.
class EventsLogActivity : AppCompatActivity() {

    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the layout defined in activity_events_log.xml.
        setContentView(R.layout.activity_events_log)
    }
}
