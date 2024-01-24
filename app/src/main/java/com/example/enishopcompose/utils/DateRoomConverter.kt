package com.example.enishopcompose.utils

import androidx.room.TypeConverter
import java.util.Date

class DateRoomConverter {

    @TypeConverter
    fun convertDateToMillis(date : Date): Long{
        return date.time
    }

    @TypeConverter
    fun convertMillisToDate(millis: Long): Date {
        return Date(millis)
    }
}