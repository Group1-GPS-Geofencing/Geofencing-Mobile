package com.geofence.geofencing_mobile.view

// Author: sibongire nyirenda (Bsc-com-ne-07-18)
// Last Modified by: James Kalulu (Bsc-com-ne-21-19)


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.geofence.geofencing_mobile.R
import com.geofence.geofencing_mobile.controller.Controller
import com.geofence.geofencing_mobile.controller.RemoteRepository
import com.geofence.geofencing_mobile.model.entities.Route
import com.geofence.geofencing_mobile.view.adapters.RoutesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Activity to display and manage routes.
 */
class RoutesActivity : AppCompatActivity() {

    private lateinit var listViewRoutes: ListView
    private lateinit var adapter: RoutesAdapter
    private lateinit var controller: Controller
    private var routes: MutableList<Route> = mutableListOf()

    /**
     * Called when the activity is first created.
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)

        // Initialize ListView and Controller
        listViewRoutes = findViewById(R.id.listViewRoutes)
        controller = Controller(RemoteRepository())

        // Set up the adapter for ListView with delete action handling
        adapter = RoutesAdapter(this, routes) { route ->
            // Launch a coroutine to handle route deletion
            CoroutineScope(Dispatchers.Main).launch {
                deleteRoute(route)
            }
        }
        listViewRoutes.adapter = adapter

        // Fetch the list of fences from the remote database
        fetchRoutes()

        // Set click listeners for various UI elements
        setOnClickListeners()

    }

    /**
     * Set click listeners for the toolbar and ListView items.
     */
    private fun setOnClickListeners() {

        // Set OnClickListener for the toolbar navigation icon (back button).
        findViewById<Toolbar>(R.id.toolbarRoutes).setNavigationOnClickListener {
            // Finish the current activity and go back to the previous activity when the navigation icon is clicked.
            finish()
        }

        //set OnclickListener for listviews items to edit a fence
        listViewRoutes.setOnItemClickListener { _, _, position, _ ->
            val route = routes[position]
            // Start EditFenceActivity with the selected route
            val intent = Intent(this, EditFenceActivity::class.java).apply {
                putExtra("route", route)
            }
            startActivity(intent)
        }
    }

    /**
     * Called when the activity resumes.
     * Refreshes the list of routes.
     */
    override fun onResume() {
        super.onResume()
        // Refresh the list of fences when the activity resumes
        fetchRoutes()
    }

    /**
     * Fetches the list of routes from the controller.
     */
    private fun fetchRoutes() {
        CoroutineScope(Dispatchers.Main).launch {
            controller.fetchRoutes(
                onSuccess = { fetchedRoutes ->
                    // Clear the current list and add fetched routes
                    routes.clear()
                    adapter.addAll(fetchedRoutes)
                    adapter.notifyDataSetChanged()
                },
                onError = { error ->
                    // Show error message if fetching routes fails
                    Toast.makeText(
                        this@RoutesActivity,
                        "Error fetching routes: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    /**
     * Deletes a route from the database and updates the UI.
     * @param route The route to be deleted.
     */
    private suspend fun deleteRoute(route: Route) {
        // Perform delete operation
        controller.deleteRoute(route,
            onSuccess = {
                // Remove the route from the list and notify the adapter
                routes.remove(route)
                adapter.notifyDataSetChanged()
                // Show success message
                Toast.makeText(this, "Route deleted successfully", Toast.LENGTH_SHORT).show()
            },
            onError = { error ->
                // Show error message if deleting route fails
                Toast.makeText(this, "Error deleting route: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    // Constant for the create fence request code
    companion object {
        private const val CREATE_FENCE_REQUEST_CODE = 1001
    }


    /**
     * Called when an activity you launched exits, giving you the requestCode.
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_FENCE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Fence created successfully, refresh the list
            fetchRoutes()
        }
    }
}
