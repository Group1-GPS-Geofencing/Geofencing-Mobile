package com.geofence.geofencing_mobile.view

// Author: sibongire nyirenda (Bsc-com-ne-07-18)
// Last Modified on: 2-06-2024


// Import necessary Android libraries.
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.geofence.geofencing_mobile.R

// HomeActivity is the main screen of the application, showing different options using CardViews.
class HomeActivity : AppCompatActivity() {

    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the layout defined in activity_home.xml.
        setContentView(R.layout.activity_home)

        // Initialize the onClickListeners for the CardViews.
        setOnClickListeners()
    }

    // Method to set onClickListeners for each CardView in the layout.
    private fun setOnClickListeners() {

        // Set OnClickListener for the Fences CardView
        findViewById<CardView>(R.id.cardFences).setOnClickListener {
            // Start the FencesActivity when the Fences CardView is clicked.
            startActivity(Intent(this, FencesActivity::class.java))
        }

        // Set OnClickListener for the Routes CardView
        findViewById<CardView>(R.id.cardRoutes).setOnClickListener {
            // Start the RoutesActivity when the Routes CardView is clicked.
            startActivity(Intent(this, RoutesActivity::class.java))
        }

        // Set OnClickListener for the EventsLog CardView
        findViewById<CardView>(R.id.cardEventLogs).setOnClickListener {
            // Start the EventsLogActivity when the EventsLog CardView is clicked.
            startActivity(Intent(this, EventsLogActivity::class.java))
        }
    }
}
