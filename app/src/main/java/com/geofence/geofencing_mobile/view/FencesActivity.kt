package com.geofence.geofencing_mobile.view

// Author: sibongire nyirenda (Bsc-com-ne-07-18)
// Last Modified on: 2-06-2024


// Import necessary Android libraries.
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.geofence.geofencing_mobile.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

// FencesActivity is a screen within the application that allows users to view and manage geofences.
class FencesActivity : AppCompatActivity() {

    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the layout defined in activity_fences.xml.
        setContentView(R.layout.activity_fences)

        // Initialize the onClickListeners for the views.
        setOnClickListeners()
    }

    // Method to set onClickListeners for various views in the layout.
    private fun setOnClickListeners() {

        // Set OnClickListener for the FloatingActionButton (FAB) to create a new fence.
        findViewById<FloatingActionButton>(R.id.fabCreateFence).setOnClickListener {
            // Start the CreateFenceActivity when the FAB is clicked.
            startActivity(Intent(this, CreateFenceActivity::class.java))
        }

        // Set OnClickListener for the toolbar navigation icon (back button).
        findViewById<Toolbar>(R.id.toolbarFences).setNavigationOnClickListener {
            // Finish the current activity and go back to the previous activity when the navigation icon is clicked.
            finish()
        }
    }
}
