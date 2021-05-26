package com.medhat.todoapp.ui.listUi

import android.content.Context
import androidx.room.Room
import com.google.common.truth.Truth
import com.medhat.todoapp.R
import com.medhat.todoapp.data.DB.RoomDB
import com.medhat.todoapp.data.DB.TodoDao
import com.medhat.todoapp.data.model.TodoModel
import com.medhat.todoapp.data.repos.TodoRepo
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
class MainViewModelTest{
    private lateinit var viewModel: MainViewModel

    private lateinit var mainRepo: TodoRepo
    @Mock
    private lateinit var dao: TodoDao
    //private lateinit var db: RoomDB


//    @Mock
//    lateinit var context: Context

    @Before
    fun setUp() {

//        db = Room.inMemoryDatabaseBuilder(context, RoomDB::class.java).allowMainThreadQueries().build()
//
//        dao = db.todoDao()

        mainRepo = TodoRepo(dao)

        viewModel = MainViewModel(mainRepo)

        runBlocking {
            dao.insertTodoItem(TodoModel(null, "Medhat", "Description", Date()))
        }
    }

    @Test
    fun testGetAllTodoItems() {
        val nnn = viewModel.getAllTodoList()
        val result = viewModel.todoList.getOrAwaitValue().find {
            it.title == "Medhat" && it.description == "Description"
        }
        Truth.assertThat(result != null).isTrue()
    }

    @Test
    fun deleteTodoItem() {
        viewModel.getAllTodoList()
        viewModel.todoList.getOrAwaitValue().let {
            viewModel.onItemDeleted(it[0])
        }

        val result: Boolean = viewModel.toastMessage.getOrAwaitValue().let {
            it == R.string.item_deleted
        }

        Truth.assertThat(result).isTrue()
    }
}