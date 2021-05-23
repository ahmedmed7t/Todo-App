package com.medhat.todoapp.data.model

interface ListDBHelper {
    suspend fun getAllTodoItems(): List<TodoModel>?
    suspend fun deleteTodoItem(item: TodoModel): Unit?
}