package com.medhat.todoapp.data.repos

import android.content.Context
import androidx.room.Room
import com.google.common.truth.Truth
import com.medhat.todoapp.data.DB.RoomDB
import com.medhat.todoapp.data.DB.TodoDao
import com.medhat.todoapp.data.model.TodoModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
class TodoRepoTest {
    private lateinit var mainRepo: TodoRepo

    private val dao: TodoDao = Mockito.mock(TodoDao::class.java)
//    private lateinit var db: RoomDB
//
//
//    private lateinit var context: Context

    @Before
    fun setUp() {
//
//        db = Room.inMemoryDatabaseBuilder(context, RoomDB::class.java).allowMainThreadQueries().build()
//
//        dao = db.todoDao()

        mainRepo = TodoRepo(dao)

        runBlocking {
            dao.insertTodoItem(TodoModel(null, "Medhat", "Description", Date()))
        }
    }

    @Test
    fun testGetAllTodoItems() = runBlocking{
        dao.insertTodoItem(TodoModel(null, "Medhat", "Description", Date()))
        dao.insertTodoItem(TodoModel(null, "Medhat", "Description", Date())).let{
            val result = dao.getAllTodoList()
            val text = ""
        }


        //Truth.assertThat(result != null && result[0].title == "Medhat").isTrue()
    }

    @Test
    fun deleteItemTodoItems() = runBlocking{
        var result = mainRepo.getAllTodoItems()
        val modelToDelete : TodoModel = result?.get(0) ?: TodoModel(null,"","", Date())

        mainRepo.deleteTodoItem(modelToDelete)

        result = mainRepo.getAllTodoItems()

        Truth.assertThat(result != null).isTrue()
    }
}