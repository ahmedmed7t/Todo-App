package com.medhat.todoapp.di

import com.medhat.todoapp.data.DB.RoomDB
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        RoomDB.getAppDataBase(androidContext())?.todoDao()
    }
}