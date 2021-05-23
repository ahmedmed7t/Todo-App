package com.medhat.todoapp.ui.addItemUi

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.medhat.todoapp.data.DB.RoomDB
import com.medhat.todoapp.data.DB.TodoDao
import com.medhat.todoapp.data.model.TodoModel
import com.medhat.todoapp.data.repos.AddTodoItemRepo
import com.medhat.todoapp.getOrAwaitValue
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import java.util.*

@RunWith(AndroidJUnit4::class)
class AddTodoItemViewModelTest : TestCase() {

    private lateinit var viewModel: AddTodoItemViewModel
    private lateinit var addItemRepo: AddTodoItemRepo
    private lateinit var roomDB: RoomDB
    private lateinit var todoDao: TodoDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        super.setUp()

        val context = ApplicationProvider.getApplicationContext<Context>()
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
            if (it) {
                runBlocking {
                    allSavedItem = todoDao.getAllTodoList()
                }
            }
        }

        assertThat(allSavedItem?.size == 2 && allSavedItem?.get(1)?.title == "second Medhat").isTrue()
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
}