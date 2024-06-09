package com.geofence.geofencing_mobile.model.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * author Bridget
 * Entity data class represents a single entity in the database.
 * entity Location
 */

@Serializable
data class GeoJsonPoint(
    val type: String = "Point",
    val coordinates: List<Double> = emptyList()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createDoubleArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeDoubleList(coordinates)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GeoJsonPoint> {
        override fun createFromParcel(parcel: Parcel): GeoJsonPoint {
            return GeoJsonPoint(parcel)
        }

        override fun newArray(size: Int): Array<GeoJsonPoint?> {
            return arrayOfNulls(size)
        }
    }
}


private fun Parcel.createDoubleArrayList(): ArrayList<Double>? {
    val size = readInt()
    return if (size >= 0) {
        val list = ArrayList<Double>()
        repeat(size) {
            list.add(readDouble())
        }
        list
    } else {
        null
    }
}

private fun Parcel.writeDoubleList(list: List<Double>) {
    writeInt(list.size)
    list.forEach { writeDouble(it) }
}



@Serializable
@Entity(
    tableName = "location",
    foreignKeys = [ForeignKey(
        entity = Route::class,
        parentColumns = ["id"],
        childColumns = ["route_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1,
    val point: GeoJsonPoint,
    val time: String,
    @ColumnInfo(name = "route_id")
    val routeId: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readParcelable(GeoJsonPoint::class.java.classLoader) ?: GeoJsonPoint(),
        parcel.readString() ?: "",
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeParcelable(point, flags)
        parcel.writeString(time)
        parcel.writeLong(routeId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}
