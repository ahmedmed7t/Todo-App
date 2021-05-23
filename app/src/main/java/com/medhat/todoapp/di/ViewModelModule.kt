package com.medhat.todoapp.di

import com.medhat.todoapp.ui.addItemUi.AddTodoItemViewModel
import com.medhat.todoapp.ui.listUi.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), androidContext())
    }

    viewModel {
        AddTodoItemViewModel(get())
    }
}