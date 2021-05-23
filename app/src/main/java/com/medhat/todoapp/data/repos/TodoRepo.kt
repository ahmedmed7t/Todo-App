package com.medhat.todoapp.data.repos

import com.medhat.todoapp.data.DB.TodoDao
import com.medhat.todoapp.data.model.ListDBHelper
import com.medhat.todoapp.data.model.TodoModel

class TodoRepo(val todoDao: TodoDao?) : ListDBHelper {
    override suspend fun getAllTodoItems() = todoDao?.getAllTodoList()

    override suspend fun deleteTodoItem(item: TodoModel) = todoDao?.deleteTodoItem(item)
}