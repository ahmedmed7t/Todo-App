package com.medhat.todoapp.data.model

interface AddNewDBHelper {
    suspend fun insertTodoItem(item: TodoModel): Long
    suspend fun updateTodoItem(item: TodoModel)

}