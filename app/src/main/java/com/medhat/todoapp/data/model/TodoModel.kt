package com.medhat.todoapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.medhat.todoapp.Utils.DateConverter
import org.koin.core.qualifier.named
import java.time.LocalDate
import java.util.*

@Entity(tableName = "Todo_Table")
data class TodoModel(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int?,
        @ColumnInfo(name = "title")
        var title: String,
        @ColumnInfo(name = "description")
        var description: String,
        @ColumnInfo(name = "time")
        var time: Date)