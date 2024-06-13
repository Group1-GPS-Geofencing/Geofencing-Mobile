package com.geofence.geofencing_mobile.view

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * EditFenceActivity allows the user to edit the details of a selected fence.
 */
class EditFenceActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fence: Fence // The selected fence object
    private lateinit var controller: Controller
    private val polygonPoints = mutableListOf<LatLng>()
    private var polygon: Polygon? = null

    /**
     * Called when the activity is first created.
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_fence)

        setSupportActionBar(findViewById(R.id.toolbarEditFence))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Retrieve the selected fence object from intent extras
        fence = intent.getParcelableExtra<Fence>("fence")!!

        // Initialize Controller with RemoteRepository
        controller = Controller(RemoteRepository())

        findViewById<TextInputEditText>(R.id.textInputFenceName).setText(fence.name)
        findViewById<TextInputEditText>(R.id.textInputFenceDescription).setText(fence.description)

        // Set OnClickListener for the Save Fence Edit button
        findViewById<Button>(R.id.btn_save_fence_edit).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                saveEditedFence()
            }
        }

        // Set OnClickListener for the Redo button
        findViewById<Button>(R.id.btn_redo).setOnClickListener {
            if (polygonPoints.isNotEmpty()) {
                polygonPoints.removeAt(polygonPoints.size - 1)
                drawPolygon()
            } else {
                Toast.makeText(this, "No points to remove", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Called when the map is ready to be used.
     * @param googleMap The GoogleMap object.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Draw the existing fence boundary on the map
        val coordinates = fence.boundary.coordinates[0]

        // Remove the last point if it is the same as the first to prevent closing the polygon
        if (coordinates.size > 1 && coordinates.first() == coordinates.last()) {
            polygonPoints.addAll(coordinates.dropLast(1).map { LatLng(it[1], it[0]) })
        } else {
            polygonPoints.addAll(coordinates.map { LatLng(it[1], it[0]) })
        }

        drawPolygon()

        // Set the camera position to focus on the fence boundary
        val boundsBuilder = LatLngBounds.Builder()
        for (coordinate in coordinates) {
            boundsBuilder.include(LatLng(coordinate[1], coordinate[0]))
        }
        val bounds = boundsBuilder.build()
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

        // Set a map click listener to add points to the polygon.
        mMap.setOnMapClickListener { latLng ->
            polygonPoints.add(latLng)
            drawPolygon()
        }
    }

    /**
     * Draws the polygon on the map using the collected points.
     */    private fun drawPolygon() {
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
     * Saves the edited fence details to the server.
     */
    private suspend fun saveEditedFence() {
        val editedName = findViewById<TextInputEditText>(R.id.textInputFenceName).text.toString()
        val editedDescription =
            findViewById<TextInputEditText>(R.id.textInputFenceDescription).text.toString()

        // Validate inputs
        if (editedName.isBlank() || editedDescription.isBlank()) {
            Toast.makeText(this, "Name and description cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Update the fence object with the edited details
        fence.name = editedName
        fence.description = editedDescription

        // Update the boundary with the new polygon points
        val newCoordinates = polygonPoints.map { listOf(it.longitude, it.latitude) }.toMutableList()

        // Close the polygon by adding the first point to the end if it's not already closed
        if (newCoordinates.first() != newCoordinates.last()) {
            newCoordinates.add(newCoordinates.first())
        }

        val updatedBoundary = GeoJsonPolygon(type = "Polygon", coordinates = listOf(newCoordinates))
        fence = fence.copy(boundary = updatedBoundary)

        // Perform the HTTP PUT request to update the fence details on the server
        controller.updateFence(fence,
            onSuccess = { updatedFence ->
                // Handle successful update
                Toast.makeText(
                    this@EditFenceActivity,
                    "Fence updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
                // Set the edited fence to active
                CoroutineScope(Dispatchers.Main).launch {
                    setFenceActive(updatedFence)
                }
            },
            onError = { error ->
                // Handle error
                Toast.makeText(
                    this@EditFenceActivity,
                    "Error updating fence: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
    /**
     * Sets the edited fence to active.
     * @param fence the edited fence to be set to active
     */
    private suspend fun setFenceActive(fence: Fence) {
        fence.isActive = true
        controller.updateFence(fence,
            onSuccess = {
                // Finish the activity after setting the fence to active
                finish()
            },
            onError = { error ->
                // Handle error
                Toast.makeText(
                    this@EditFenceActivity,
                    "Error setting fence to active: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    /**
     * Handles toolbar navigation (back) button click.
     * @param item The menu item that was selected.
     * @return True if the item was handled, false otherwise.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
