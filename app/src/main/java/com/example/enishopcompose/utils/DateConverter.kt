package com.example.enishopcompose.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object DateConverter {
    val formatter = SimpleDateFormat("dd/MM/yyyy")


    fun convertDateToMillis(date : Date): Long{
        return date.time
    }

    fun convertMillisToDate(millis: Long): Date {
        return Date(millis)
    }

    @ToJson
    fun convertDateToSimpleString(date : Date): String {
        return formatter.format(date)
    }

    @FromJson
    fun convertStringToSimpleDate(text : String): Date {
        var date = Date()
        try {
            date = formatter.parse(text)
        }catch (e: ParseException){

        }
        return date
    }

}