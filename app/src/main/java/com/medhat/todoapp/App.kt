package com.medhat.todoapp

import android.app.Application
import com.medhat.todoapp.di.appModule
import com.medhat.todoapp.di.repoModule
import com.medhat.todoapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule,repoModule, viewModelModule))
        }
    }
}