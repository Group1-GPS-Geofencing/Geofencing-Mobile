package com.geofence.geofencing_mobile.view

// Author: sibongire nyirenda (Bsc-com-ne-07-18)
// Last Modified on: 2-06-2024


// Import necessary Android libraries.
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.geofence.geofencing_mobile.R

// RoutesActivity is a screen within the application that displays a list of routes using a RecyclerView.
class RoutesActivity : AppCompatActivity() {

    // Declare a lateinit variable for the RecyclerView that will display the routes.
    private lateinit var recyclerViewRoutes: RecyclerView

    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the layout defined in activity_routes.xml.
        setContentView(R.layout.activity_routes)

        // Initialize the RecyclerView by finding it in the layout using its ID.
        recyclerViewRoutes = findViewById(R.id.recyclerViewRoutes)
    }

}
