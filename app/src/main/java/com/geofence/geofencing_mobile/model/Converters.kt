package com.geofence.geofencing_mobile.model

import androidx.room.TypeConverter
import java.sql.Timestamp

/**
 * Author Bridget faindani
 * converter class
 * convert data from to timestamp
 */
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it) }
    }

    @TypeConverter
    fun dateToTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.time
    }
}
