package com.geofence.geofencing_mobile.model.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Author Bridget Faindani
 * Last Modified by James Kalulu (Bsc-com-ne-21-19)
 * A data class representing a GeoJSON LineString object.
 *
 * @property type The type of the GeoJSON object, which is always "LineString".
 * @property coordinates A list of coordinate pairs, where each pair is a list of doubles representing a point.
 */

@Serializable
data class GeoJsonLineString(
    val type: String = "LineString",
    val coordinates: List<List<Double>>?
) : Parcelable {
    /**
     * Constructor to create a GeoJsonLineString object from a Parcel.
     *
     * @param parcel The parcel to read the object's data from.
     */
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createListOfListOfDouble()
    )

    /**
     * Default constructor to create a GeoJsonLineString object with empty coordinates.
     */
    constructor() : this("", emptyList())

    /**
     * Write the object's data to a Parcel.
     *
     * @param parcel The parcel to write the object's data to.
     * @param flags Additional flags about how the object should be written.
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        if (coordinates != null) {
            parcel.writeListOfListOfDouble(coordinates)
        }
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     *
     * @return A bitmask indicating the set of special object types marshaled by the Parcelable.
     */
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GeoJsonLineString> {
        /**
         * Create a new instance of the GeoJsonLineString class, instantiating it from the given Parcel.
         *
         * @param parcel The Parcel to read the object's data from.
         * @return A new instance of the GeoJsonLineString class.
         */
        override fun createFromParcel(parcel: Parcel): GeoJsonLineString {
            return GeoJsonLineString(parcel)
        }

        /**
         * Create a new array of the GeoJsonLineString class.
         *
         * @param size The size of the array.
         * @return An array of the GeoJsonLineString class, with every entry initialized to null.
         */
        override fun newArray(size: Int): Array<GeoJsonLineString?> {
            return arrayOfNulls(size)
        }

        /**
         * Create a default GeoJsonLineString object with empty coordinates.
         *
         * @return A default GeoJsonLineString object.
         */
        fun default(): GeoJsonLineString {
            return GeoJsonLineString("", emptyList())
        }
    }
}

/**
 * Extension function to create a list of list of doubles from a Parcel.
 *
 * @return A list of list of doubles read from the Parcel.
 */
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

/**
 * Extension function to write a list of list of doubles to a Parcel.
 *
 * @param list The list of list of doubles to write to the Parcel.
 */
private fun Parcel.writeListOfListOfDouble(list: List<List<Double>>) {
    writeInt(list.size)
    list.forEach { innerList ->
        writeInt(innerList.size)
        innerList.forEach { writeDouble(it) }
    }
}

/**
 * A data class representing a Route entity for the database.
 *
 * @property id The primary key of the route, auto-generated.
 * @property name The name of the route.
 * @property startTime The start time of the route.
 * @property points The GeoJsonLineString object representing the route's points.
 */
@Entity(tableName = "route")
@Serializable
data class Route(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @SerialName("start_time")
    val startTime: String,
    val points: GeoJsonLineString = GeoJsonLineString.default()
) : Parcelable {
    /**
     * Constructor to create a Route object from a Parcel.
     *
     * @param parcel The parcel to read the object's data from.
     */
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(GeoJsonLineString::class.java.classLoader) ?: GeoJsonLineString()
    )

    /**
     * Write the object's data to a Parcel.
     *
     * @param parcel The parcel to write the object's data to.
     * @param flags Additional flags about how the object should be written.
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(startTime)
        parcel.writeParcelable(points, flags)
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     *
     * @return A bitmask indicating the set of special object types marshaled by the Parcelable.
     */
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Route> {
        /**
         * Create a new instance of the Route class, instantiating it from the given Parcel.
         *
         * @param parcel The Parcel to read the object's data from.
         * @return A new instance of the Route class.
         */
        override fun createFromParcel(parcel: Parcel): Route = Route(parcel)

        /**
         * Create a new array of the Route class.
         *
         * @param size The size of the array.
         * @return An array of the Route class, with every entry initialized to null.
         */
        override fun newArray(size: Int): Array<Route?> = arrayOfNulls(size)
    }
}
