package com.medhat.todoapp.data.model

interface ListCaller {
    fun onItemDeleted(item: TodoModel)
    fun onItemClicked(item: TodoModel)
}