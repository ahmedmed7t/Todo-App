package com.medhat.todoapp.data.DB

import androidx.room.*
import com.medhat.todoapp.data.model.TodoModel
import java.util.*

@Dao
interface TodoDao {

    @Query("SELECT * FROM Todo_Table")
    suspend fun getAllTodoList(): List<TodoModel>?

    @Insert
    suspend fun insertTodoItem(item: TodoModel?): Long

    @Delete
    suspend fun deleteTodoItem(item: TodoModel?): Unit?

    @Query("UPDATE Todo_Table SET title = :newTitle, description= :newDescription, time = :newTime WHERE id = :oldId")
    suspend fun updateTodoItem(oldId: Int?, newTitle: String, newDescription: String, newTime: Date)
}