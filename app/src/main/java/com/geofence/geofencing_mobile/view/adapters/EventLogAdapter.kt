package com.geofence.geofencing_mobile.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.geofence.geofencing_mobile.R
import com.geofence.geofencing_mobile.model.entities.EventLog

/**
 * EventLogAdapter is responsible for displaying event log items in a list.
 * @param context The context in which the adapter is running.
 * @param data The list of event logs to be displayed.
 */

class EventLogAdapter(
    private val context: Context,
    private val data: List<EventLog>
) : BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): EventLog = data[position]

    override fun getItemId(position: Int): Long = data[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_event_log, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val eventLog = getItem(position)
        viewHolder.icon.setImageResource(R.drawable.ic_notifications_24) // Use a suitable icon
        viewHolder.header.text = eventLog.message
        viewHolder.description.text = "Description for event log ${eventLog.id}"
        viewHolder.date.text = eventLog.time.split(" ")[0]
        viewHolder.time.text = eventLog.time.split(" ")[1]

        return view
    }

    private class ViewHolder(view: View) {
        val icon: ImageView = view.findViewById(R.id.event_log_icon)
        val header: TextView = view.findViewById(R.id.event_log_header)
        val description: TextView = view.findViewById(R.id.event_log_description)
        val date: TextView = view.findViewById(R.id.event_log_date)
        val time: TextView = view.findViewById(R.id.event_log_time)
        val more: ImageView = view.findViewById(R.id.event_log_more)
    }
}
