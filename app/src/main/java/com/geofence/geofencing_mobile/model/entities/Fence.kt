package com.geofence.geofencing_mobile.model.entities

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * author Bridget Faindani
 * Entity data class represents a single row in the database.
 * entity Fence
 */

@Serializable
data class GeoJsonPolygon(
    val type: String = "Polygon",
    val coordinates: List<List<List<Double>>>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createListOfListOfListOfDouble()
    )

    // Default constructor
    constructor() : this("", emptyList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeListOfListOfListOfDouble(coordinates)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<GeoJsonPolygon> {
        override fun createFromParcel(parcel: Parcel): GeoJsonPolygon {
            return GeoJsonPolygon(parcel)
        }

        override fun newArray(size: Int): Array<GeoJsonPolygon?> {
            return arrayOfNulls(size)
        }

        fun default(): GeoJsonPolygon {
            return GeoJsonPolygon("", emptyList())
        }
    }
}

private fun Parcel.writeListOfListOfListOfDouble(value: List<List<List<Double>>>) {

    writeInt(value.size)
    value.forEach { innerList ->
        writeInt(innerList.size)
        innerList.forEach { innerInnerList ->
            writeInt(innerInnerList.size)
            innerInnerList.forEach { writeDouble(it) }
        }
    }
}

private fun Parcel.createListOfListOfListOfDouble(): List<List<List<Double>>> {

    val size = readInt()
    return List(size) {
        val innerSize = readInt()
        List(innerSize) {
            val innerInnerSize = readInt()
            List(innerInnerSize) {
                readDouble()
            }
        }
    }
}

@Serializable
data class Fence(
    val id: Long? = null,
    var name: String,
    var description: String,
    @SerialName("boundary")
    val boundary: GeoJsonPolygon = GeoJsonPolygon.default(),
    @SerialName("is_active")
    var isActive: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(GeoJsonPolygon::class.java.classLoader) ?: GeoJsonPolygon(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeParcelable(boundary, flags)
        parcel.writeByte(if (isActive) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Fence> {
        override fun createFromParcel(parcel: Parcel): Fence {
            return Fence(parcel)
        }

        override fun newArray(size: Int): Array<Fence?> {
            return arrayOfNulls(size)
        }
    }
}
