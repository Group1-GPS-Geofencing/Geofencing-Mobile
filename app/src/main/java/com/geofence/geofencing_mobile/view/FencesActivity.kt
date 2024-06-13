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
import com.geofence.geofencing_mobile.model.entities.Fence
import com.geofence.geofencing_mobile.view.adapters.FenceAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * FencesActivity displays and manages a list of fences.
 */
class FencesActivity : AppCompatActivity() {

    private lateinit var listViewFences: ListView
    private lateinit var controller: Controller
    private lateinit var adapter: FenceAdapter
    private var fences: MutableList<Fence> = mutableListOf()

    /**
     * Called when the activity is first created.
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fences)

        listViewFences = findViewById(R.id.listViewFences)
        controller = Controller(RemoteRepository())

        // Set up the adapter for ListView with delete action handling
        adapter = FenceAdapter(this, fences,
            onDelete = { fence ->
                CoroutineScope(Dispatchers.Main).launch {
                    deleteFence(fence)
                }
            },
            onToggleActive = { fence ->
                CoroutineScope(Dispatchers.Main).launch {
                    toggleActiveFence(fence)
                }
            }
        )
        listViewFences.adapter = adapter

        // Fetch the list of fences from the database
        fetchFences()

        // Set click listeners for various UI elements
        setOnClickListeners()
    }

    /**
     * Sets onClickListeners for various views in the layout.
     */
    private fun setOnClickListeners() {
        findViewById<FloatingActionButton>(R.id.fabCreateFence).setOnClickListener {
            startActivity(Intent(this, CreateFenceActivity::class.java))
        }

        findViewById<Toolbar>(R.id.toolbarFences).setNavigationOnClickListener {
            finish()
        }

        listViewFences.setOnItemClickListener { _, _, position, _ ->
            val fence = fences[position]
            // Start EditFenceActivity with the selected fence
            val intent = Intent(this, EditFenceActivity::class.java).apply {
                putExtra("fence", fence)
            }
            startActivity(intent)
        }
    }

    /**
     * Called when the activity resumes.
     * Refreshes the list of fences.
     */
    override fun onResume() {
        super.onResume()
        // Refresh the list of fences when the activity resumes
        fetchFences()
    }

    /**
     * fetches the list of fences from the controller.
     */
    private fun fetchFences() {
        CoroutineScope(Dispatchers.Main).launch {
            controller.fetchFences(
                onSuccess = { fetchedFences ->
                    fences.clear()
                    adapter.addAll(fetchedFences)
                    adapter.notifyDataSetChanged()
                },
                onError = { error ->
                    Toast.makeText(this@FencesActivity, "Error fetching fences: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    /**
     * Deletes a fence from the database and updates the UI.
     * @param fence The fence to be deleted.
     */
    private suspend fun deleteFence(fence: Fence) {
        // Perform delete operation
        controller.deleteFence(fence,
            onSuccess = {
                // Remove the fence from the list and notify the adapter
                fences.remove(fence)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Fence deleted successfully", Toast.LENGTH_SHORT).show()
            },
            onError = { error ->
                // Handle error
                Toast.makeText(this, "Error deleting fence: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

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
            fetchFences()
        }
    }

    private suspend fun toggleActiveFence(fence: Fence) {
        fence.isActive = !fence.isActive
        // Implement the activation/deactivation logic here
        controller.updateFence(fence,
            onSuccess = {
                fetchFences()
                Toast.makeText(this, "Fence status updated successfully", Toast.LENGTH_SHORT).show()
            },
            onError = { error ->
                Toast.makeText(this, "Error updating fence status: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

}
