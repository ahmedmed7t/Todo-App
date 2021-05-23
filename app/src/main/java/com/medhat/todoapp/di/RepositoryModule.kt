package com.medhat.todoapp.di

import com.medhat.todoapp.data.repos.AddTodoItemRepo
import com.medhat.todoapp.data.repos.TodoRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {

    single {
        TodoRepo(get())
    }

    single {
        AddTodoItemRepo(get())
    }
}