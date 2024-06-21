package com.geofence.geofencing_mobile.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import com.geofence.geofencing_mobile.R
import com.geofence.geofencing_mobile.model.entities.EventLog

/**
 * EventLogAdapter is responsible for displaying event log items in a list.
 * @param context The context in which the adapter is running.
 * @param data The list of event logs to be displayed.
 */

class EventLogAdapter(
    context: Context,
    private val logs: MutableList<EventLog>,
    private val onDelete: (EventLog) -> Unit
) : ArrayAdapter<EventLog>(context, 0, logs) {

    /**
     * Returns the view for a specific item in the list.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent view that this view will eventually be attached to.
     * @return The view corresponding to the data at the specified position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_event_log, parent, false)

        Log.d("EventLogAdapter", "Position: $position, List Size: ${logs.size}")
        val log = getItem(position)!!

        val logHeader = view.findViewById<TextView>(R.id.event_log_header)
        val logDescription = view.findViewById<TextView>(R.id.event_log_description)
        val logDate = view.findViewById<TextView>(R.id.event_log_date)
        val logTime = view.findViewById<TextView>(R.id.event_log_time)
        val moreOptionsImageView = view.findViewById<ImageView>(R.id.event_log_more)

        logHeader.text = log.message
        logDescription.text = log.message
        logDate.text = log.time.split("T")[0]
        logTime.text = log.time.split("T")[1]

        moreOptionsImageView.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_fence_options, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        onDelete(log)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

        return view
    }
}

