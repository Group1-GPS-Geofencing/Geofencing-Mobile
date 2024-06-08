package com.geofence.geofencing_mobile.view
// Author: sibongire nyirenda (Bsc-com-ne-07-18)
// Last Modified by: James Kalulu (Bsc-com-ne-21-19)


import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.geofence.geofencing_mobile.R
import com.geofence.geofencing_mobile.controller.Controller
import com.geofence.geofencing_mobile.controller.RemoteRepository
import com.geofence.geofencing_mobile.model.entities.Fence
import com.geofence.geofencing_mobile.model.entities.GeoJsonPolygon
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * CreateFenceActivity allows the user to create a new geofence by defining a polygon on a map.
 */
class CreateFenceActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val polygonPoints = mutableListOf<LatLng>()
    private var polygon: Polygon? = null
    private lateinit var controller: Controller

    /**
     * Called when the activity is first created.
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_fence)

        controller = Controller(RemoteRepository())

        // Initialize the onClickListeners for the views.
        setOnClickListeners()

        // Initialize the map fragment and set up the callback for when the map is ready.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Sets onClickListeners for various views in the layout.
     */
    private fun setOnClickListeners() {
        // Set OnClickListener for the toolbar navigation icon (back button).
        findViewById<Toolbar>(R.id.toolbarCreateFence).setNavigationOnClickListener {
            finish()
        }

        // Set OnClickListener for the button to complete the polygon.
        findViewById<Button>(R.id.btn_redo).setOnClickListener {
            if (polygonPoints.isNotEmpty()) {
                polygonPoints.removeAt(polygonPoints.size - 1)
                drawPolygon()
            }
        }

        // Set OnClickListener for the button to save the fence.
        findViewById<Button>(R.id.btn_save_fence).setOnClickListener {
            // Launch a coroutine
            CoroutineScope(Dispatchers.Main).launch {
                saveFence()
            }
        }
    }

    /**
     * Called when the map is ready to be used.
     * @param googleMap The GoogleMap object.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Move the camera to a default location and zoom level.
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    -15.38752770409667,
                    35.33675830762081
                ), 10f
            )
        )

        // Set a map click listener to add points to the polygon.
        mMap.setOnMapClickListener { latLng ->
            // Add the clicked point to the list of polygon points.
            polygonPoints.add(latLng)
            drawPolygon()
        }
    }

    /**
     * Draws the polygon on the map using the collected points.
     */
    private fun drawPolygon() {
        // Remove the existing polygon if there is one.
        polygon?.remove()

        // Create PolygonOptions to define the appearance of the polygon.
        if (polygonPoints.isNotEmpty()) {
            val polygonOptions =
                PolygonOptions().addAll(polygonPoints)
                    .strokeColor(Color.RED)
                    .fillColor(0x7FFF0000)
                    .strokeWidth(5f)

            // Add the polygon to the map.
            polygon = mMap.addPolygon(polygonOptions)
        }
    }

    /**
     * Saves the created fence to the server.
     */
    private suspend fun saveFence() {
        Toast.makeText(this, "Saving Fence", Toast.LENGTH_SHORT).show()
        val fenceName = findViewById<TextInputEditText>(R.id.textInputFenceName).text.toString()
        val description =
            findViewById<TextInputEditText>(R.id.textInputFenceDescription).text.toString()
        val isActive = true

        if (fenceName.isNotEmpty() && description.isNotEmpty() && polygonPoints.isNotEmpty()) {
            // Ensure the polygon is closed
            if (polygonPoints.size >= 3 && polygonPoints.first() != polygonPoints.last()) {
                polygonPoints.add(polygonPoints.first())
            }

            // Convert polygon points to GeoJSON coordinates
            val coordinates = polygonPoints.map { listOf(it.longitude, it.latitude) }
            val geoJsonPolygon = GeoJsonPolygon(
                coordinates = listOf(coordinates)
            )

            val fence = Fence(
                name = fenceName,
                description = description,
                boundary = geoJsonPolygon,
                isActive = isActive
            )

            // Check if there are enough points to create a fence
            if (polygonPoints.size >= 4) {
                // Call the controller method to save the fence
                controller.createFence(fence,
                    onSuccess = { fenceId ->
                        Toast.makeText(this, "Fence saved with ID: $fenceId", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    },
                    onError = { error ->
                        Toast.makeText(
                            this,
                            "Error saving fence: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            } else {
                Toast.makeText(this, "Please define at least 3 points for the fence", Toast.LENGTH_LONG).show()
                return
            }
        }
    }

}
