package com.geofence.geofencing_mobile.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import com.geofence.geofencing_mobile.R
import com.geofence.geofencing_mobile.model.entities.Route

/**
 * RoutesAdapter is a custom ArrayAdapter for displaying a list of routes.
 * @param context The context in which the adapter is being used.
 * @param routes The list of routes to be displayed.
 * @param onDelete A lambda function to handle the delete action for a route.
 */
class RoutesAdapter(
    context: Context,
    private val routes: MutableList<Route>,
    private val onDelete: (Route) -> Unit
) : ArrayAdapter<Route>(context, 0, routes) {

    /**
     * Returns the view for a specific item in the list.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent view that this view will eventually be attached to.
     * @return The view corresponding to the data at the specified position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_route_layout, parent, false)
        val route = getItem(position)!!

        val routeName = view.findViewById<TextView>(R.id.routeName)
        val moreOptionsImageView = view.findViewById<ImageView>(R.id.iconMore)

        routeName.text = route.name

        moreOptionsImageView.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_fence_options, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        onDelete(route)
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
