package com.medhat.todoapp.Utils

import androidx.room.TypeConverter
import java.time.LocalDate
import java.util.*


class DateConverter {

    @TypeConverter
    fun fromDateToString(date: Date): String? = DateUtils.getFormattedDate(date)

    @TypeConverter
    fun fromStringToDate(date: String): Date? = DateUtils.getDateFromString(date)

}