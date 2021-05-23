package com.medhat.todoapp.ui.listUi

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.medhat.todoapp.R
import com.medhat.todoapp.data.DB.RoomDB
import com.medhat.todoapp.data.DB.TodoDao
import com.medhat.todoapp.data.model.TodoModel
import com.medhat.todoapp.data.repos.TodoRepo
import com.medhat.todoapp.getOrAwaitValue
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


@RunWith(AndroidJUnit4::class)
class MainViewModelTest : TestCase() {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainRepo: TodoRepo
    private lateinit var dao: TodoDao
    private lateinit var db: RoomDB
    private lateinit var context: Context

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        super.setUp()
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RoomDB::class.java).allowMainThreadQueries().build()

        dao = db.todoDao()

        mainRepo = TodoRepo(dao)

        viewModel = MainViewModel(mainRepo, context)

        runBlocking {
            dao.insertTodoItem(TodoModel(null, "Medhat", "Description", Date()))
        }
    }

    @Test
    fun testGetAllTodoItems() {
        viewModel.getAllTodoList()
        val result = viewModel.todoList.getOrAwaitValue().find {
            it.title == "Medhat" && it.description == "Description"
        }
        assertThat(result != null).isTrue()
    }

    @Test
    fun deleteTodoItem() {
        viewModel.getAllTodoList()
        viewModel.todoList.getOrAwaitValue().let {
            viewModel.onItemDeleted(it[0])
        }

        val result: Boolean = viewModel.toastMessage.getOrAwaitValue().let {
            it == context.getString(R.string.item_deleted)
        }

        assertThat(result).isTrue()
    }
}