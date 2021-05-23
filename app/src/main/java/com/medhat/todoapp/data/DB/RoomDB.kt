package com.medhat.todoapp.data.DB

import android.content.Context
import androidx.room.*
import com.medhat.todoapp.Utils.DateConverter
import com.medhat.todoapp.data.model.TodoModel

@Database(entities = [TodoModel::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        private var INSTANCE: RoomDB? = null

        fun getAppDataBase(context: Context): RoomDB? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder<RoomDB>(context, RoomDB::class.java, "RoomDB")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build()
            }

            return INSTANCE
        }
    }

}