package com.geofence.geofencing_mobile.view

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.geofence.geofencing_mobile.R
import com.geofence.geofencing_mobile.model.entities.Route
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Activity to display the route on a Google Map.
 */
class RouteViewActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var route: Route

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_view)

        setSupportActionBar(findViewById(R.id.toolbarRouteView))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Retrieve the selected fence object from intent extras
        route = intent.getParcelableExtra<Route>("route")!!

        // Set the toolbar title to the route name
        findViewById<Toolbar>(R.id.toolbarRouteView).title = route.name

        // Set the start and end times in the TextViews
        findViewById<TextView>(R.id.routeStartTime).text = formatTimestamp(route.startTime)
        findViewById<TextView>(R.id.routeEndTime).text = "N/A"


        setOnClickListeners()
    }


    /**
     * Sets onClickListeners for various views in the layout.
     */
    private fun setOnClickListeners() {
        // Set OnClickListener for the toolbar navigation icon (back button).
        findViewById<Toolbar>(R.id.toolbarRouteView).setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * Called when the map is ready to be used.
     * @param googleMap The GoogleMap object representing the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        drawRouteOnMap()
    }

    /**
     * Draws the route on the map by adding a polyline representing the route's LineString points.
     */
    private fun drawRouteOnMap() {
        val polylineOptions = PolylineOptions().apply {
            color(Color.BLUE)
            width(5f)
            route.points.coordinates?.forEach { point ->
                // Swap latitude and longitude values
                val latLng = LatLng(point[0], point[1])
                add(latLng)
                // Add a marker at each point
                mMap.addMarker(MarkerOptions().position(latLng))
            }
        }
        mMap.addPolyline(polylineOptions)

        // Move camera to the first point in the route
        if (route.points.coordinates?.isNotEmpty() == true) {
            val firstPoint = route.points.coordinates!!.first()
            val firstLatLng = LatLng(firstPoint[0], firstPoint[1])
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10f))
        }
    }

    /**
     * Formats a timestamp string to a more readable format.
     * @param timestamp The timestamp string to be formatted.
     * @return The formatted timestamp string.
     */
    private fun formatTimestamp(timestamp: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault())
        return try {
            val date: Date = inputFormat.parse(timestamp)!!
            outputFormat.format(date)
        } catch (e: Exception) {
            timestamp // If parsing fails, return the original string
        }
    }

}
