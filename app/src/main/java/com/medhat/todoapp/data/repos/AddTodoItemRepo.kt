package com.medhat.todoapp.data.repos

import com.medhat.todoapp.data.DB.TodoDao
import com.medhat.todoapp.data.model.AddNewDBHelper
import com.medhat.todoapp.data.model.TodoModel

class AddTodoItemRepo(private val todoDao: TodoDao) : AddNewDBHelper {
    override suspend fun insertTodoItem(item: TodoModel) = todoDao.insertTodoItem(item)

    override suspend fun updateTodoItem(item: TodoModel) = todoDao.updateTodoItem(item.id, item.title, item.description, item.time)
}