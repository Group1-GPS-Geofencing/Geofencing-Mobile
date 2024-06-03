package com.geofence.geofencing_mobile.entities

import androidx.room.TypeConverters
import java.util.Date

class DateConverter {
    @TypeConverters
    fun fromTimestamp(value: Long?): Date?{
        return value?.let {Date(it)}
    }
    @TypeConverters
    fun  dateToTimestamp(date: Date?): Long? {
        return  date?.time
    }
}