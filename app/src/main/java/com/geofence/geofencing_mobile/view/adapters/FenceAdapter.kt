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
import com.geofence.geofencing_mobile.model.entities.Fence

/**
 * FenceAdapter is a custom ArrayAdapter for displaying a list of fences.
 * @param context The context in which the adapter is being used.
 * @param fences The list of fences to be displayed.
 * @param onDelete A lambda function to handle the delete action for a fence.
 */
class FenceAdapter(
    context: Context,
    private val fences: MutableList<Fence>,
    private val onDelete: (Fence) -> Unit,
    private val onToggleActive: (Fence) -> Unit
) : ArrayAdapter<Fence>(context, 0, fences) {

    /**
     * Returns the view for a specific item in the list.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent view that this view will eventually be attached to.
     * @return The view corresponding to the data at the specified position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_fence_layout, parent, false)
        val fence = getItem(position)!!

        val fenceNameTextView = view.findViewById<TextView>(R.id.fenceName)
        val fenceDescriptionTextView = view.findViewById<TextView>(R.id.fenceDescription)
        val moreOptionsImageView = view.findViewById<ImageView>(R.id.iconFencesMore)
        val activeIconImageView = view.findViewById<ImageView>(R.id.iconFenceActive)

        fenceNameTextView.text = fence.name
        fenceDescriptionTextView.text = fence.description

        moreOptionsImageView.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_fence_options, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        onDelete(fence)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

        if (fence.isActive) {
            activeIconImageView.visibility = View.VISIBLE
        } else {
            activeIconImageView.visibility = View.INVISIBLE
        }

        // Handle the click event for active icon
        activeIconImageView.setOnClickListener {
            // Prevent the click event from propagating to the parent view
            it.isClickable = true
            it.isFocusable = true
            onToggleActive(fence)
        }

        return view
    }
}
