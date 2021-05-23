package com.medhat.todoapp.Utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

object DateUtils {
    fun getFormattedDate(date: Date) : String{
        val sdf = SimpleDateFormat("hh.mm aa dd/M/yyyy")
        return sdf.format(date)
    }

    fun getDateFromString(stringDate: String ) : Date?{
        val format = SimpleDateFormat("hh.mm aa dd/M/yyyy")
        var date : Date? = null
        try {
            date = format.parse(stringDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }
}