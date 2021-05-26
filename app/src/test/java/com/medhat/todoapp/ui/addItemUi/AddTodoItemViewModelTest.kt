package com.medhat.todoapp.ui.addItemUi

import android.content.Context
import androidx.room.Room
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.medhat.todoapp.data.DB.RoomDB
import com.medhat.todoapp.data.DB.TodoDao
import com.medhat.todoapp.data.model.TodoModel
import com.medhat.todoapp.data.repos.AddTodoItemRepo
import com.medhat.todoapp.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class AddTodoItemViewModelTest {

    private lateinit var viewModel: AddTodoItemViewModel
    private lateinit var addItemRepo: AddTodoItemRepo
    private lateinit var roomDB: RoomDB
    private lateinit var todoDao: TodoDao

    @Mock
    lateinit var context: Context

    @Before
    fun setUp() {


        roomDB = Room.inMemoryDatabaseBuilder(context, RoomDB::class.java).allowMainThreadQueries().build()
        todoDao = roomDB.todoDao()

        addItemRepo = AddTodoItemRepo(todoDao)

        viewModel = AddTodoItemViewModel(addItemRepo)
        runBlocking {
            todoDao.insertTodoItem(TodoModel(null, "Medhat", "description", Date()))
        }
    }

    @Test
    fun addNewItemInDB() {
        val todoModel = TodoModel(null, "second Medhat", "second description", Date())
        var allSavedItem: List<TodoModel>? = null
        viewModel.addTodoItem(todoModel, true)

        viewModel.isProcessFinished.getOrAwaitValue().let {
                runBlocking {
                    allSavedItem = todoDao.getAllTodoList()
                }
        }

        Truth.assertThat(allSavedItem?.size == 2 && allSavedItem?.get(1)?.title == "second Medhat").isTrue()
    }

    @Test
    fun updateItemInDB() {
        var allSavedItem: List<TodoModel>?
        runBlocking {
            allSavedItem = todoDao.getAllTodoList()
        }

        val todoModel = TodoModel(allSavedItem?.get(0)?.id, "Ahmed Medhat", "full description", Date())
        viewModel.addTodoItem(todoModel, false)

        val result = viewModel.isProcessFinished.getOrAwaitValue().let {
            runBlocking {
                allSavedItem = todoDao.getAllTodoList()
            }
        }

        assertThat(allSavedItem?.size == 1 && allSavedItem?.get(0)?.title == "Ahmed Medhat").isTrue()
    }

    @Test
    fun canAddTodoItem(){
        viewModel.canAddTodOItem("Medhat","description","02.50 AM 25/5/2021",
                null, true)

        var isValidItem : Boolean = false
        viewModel.validationError.getOrAwaitValue().let {
            isValidItem = it
        }
        assertThat(isValidItem).isTrue()
    }

    @Test
    fun canNotAddTodoItem(){
        viewModel.canAddTodOItem("Medhat","","02.50 AM 25/5/2021",
                null, true)

        var isValidItem : Boolean
        viewModel.validationError.getOrAwaitValue().let {
            isValidItem = it
        }
        assertThat(isValidItem).isFalse()
    }
}