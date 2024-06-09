package com.geofence.geofencing_mobile.model.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Author Bridget Faindani
 * Entity data class represents a single entity in the database.
 * Route entity
 */

@Serializable
data class GeoJsonLineString(
    val type: String = "LineString",
    val coordinates: List<List<Double>> = emptyList()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createListOfListOfDouble() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeListOfListOfDouble(coordinates)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GeoJsonLineString> {
        override fun createFromParcel(parcel: Parcel): GeoJsonLineString {
            return GeoJsonLineString(parcel)
        }

        override fun newArray(size: Int): Array<GeoJsonLineString?> {
            return arrayOfNulls(size)
        }
    }
}

private fun Parcel.createListOfListOfDouble(): List<List<Double>>? {
    val size = readInt()
    return if (size >= 0) {
        val list = mutableListOf<List<Double>>()
        repeat(size) {
            val innerSize = readInt()
            val innerList = mutableListOf<Double>()
            repeat(innerSize) {
                innerList.add(readDouble())
            }
            list.add(innerList)
        }
        list
    } else {
        null
    }
}

private fun Parcel.writeListOfListOfDouble(list: List<List<Double>>) {
    writeInt(list.size)
    list.forEach { innerList ->
        writeInt(innerList.size)
        innerList.forEach { writeDouble(it) }
    }
}

@Entity(tableName = "route")
@Serializable
data class Route(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @SerialName("start_time")
    val startTime: String,
    val points: String,
    val locations: List<Location>
) : Parcelable {
    // Function to convert timestamp to human-readable date string
    fun getFormattedStartTime(): String {
        // Parse ISO-8601 date format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val date = dateFormat.parse(startTime)
        // Format the date in desired format
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
    }

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        mutableListOf<Location>().apply {
            parcel.readList(this, Location::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(startTime)
        parcel.writeString(points)
        parcel.writeList(locations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Route> {
        override fun createFromParcel(parcel: Parcel): Route {
            return Route(parcel)
        }

        override fun newArray(size: Int): Array<Route?> {
            return arrayOfNulls(size)
        }
    }
}
