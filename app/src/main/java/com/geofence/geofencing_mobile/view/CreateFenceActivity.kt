package com.geofence.geofencing_mobile.view
// Author: sibongire nyirenda (Bsc-com-ne-07-18)
// Last Modified on: 2-06-2024


// Import necessary Android libraries.
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.geofence.geofencing_mobile.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions

// CreateFenceActivity is a screen within the application that allows users to create geofences on a map.
class CreateFenceActivity : AppCompatActivity(), OnMapReadyCallback {

    // Declare variables for the GoogleMap object and a list to store polygon points.
    private lateinit var mMap: GoogleMap
    private val polygonPoints = mutableListOf<LatLng>()
    private var polygon: Polygon? = null

    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the layout defined in activity_create_fence.xml.
        setContentView(R.layout.activity_create_fence)

        // Initialize the onClickListeners for the views.
        setOnClickListeners()

        // Initialize the map fragment and set up the callback for when the map is ready.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Method to set onClickListeners for various views in the layout.
    private fun setOnClickListeners() {
        // Set OnClickListener for the toolbar navigation icon (back button).
        findViewById<Toolbar>(R.id.toolbarCreateFence).setNavigationOnClickListener {
            // Finish the current activity and go back to the previous activity when the navigation icon is clicked.
            finish()
        }

        // Set OnClickListener for the button to complete the polygon.
        findViewById<Button>(R.id.btn_complete_polygon).setOnClickListener {
            // Logic to complete the polygon can be added here.
        }
    }

    // onMapReady is called when the map is ready to be used.
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Move the camera to a default location and zoom level.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.0, 151.0), 10f))

        // Set a map click listener to add points to the polygon.
        mMap.setOnMapClickListener { latLng ->
            // Add the clicked point to the list of polygon points.
            polygonPoints.add(latLng)
            // Draw the polygon on the map with the updated points.
            drawPolygon()
        }
    }

    // Method to draw the polygon on the map using the collected points.
    private fun drawPolygon() {
        // Remove the existing polygon if there is one.
        polygon?.remove()

        // Create PolygonOptions to define the appearance of the polygon.
        val polygonOptions = PolygonOptions()
            .addAll(polygonPoints) // Add all the collected points to the polygon.
            .strokeColor(Color.RED) // Set the stroke color of the polygon.
            .fillColor(0x7FFF0000) // Set the fill color of the polygon with some transparency.
            .strokeWidth(5f) // Set the stroke width of the polygon.

        // Add the polygon to the map.
        polygon = mMap.addPolygon(polygonOptions)
    }
}
