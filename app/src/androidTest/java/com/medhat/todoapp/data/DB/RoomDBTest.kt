package com.medhat.todoapp.data.DB

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.medhat.todoapp.data.model.TodoModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
class RoomDBTest : TestCase() {

    private lateinit var db: RoomDB
    private lateinit var dao: TodoDao
    private val dateFormat = SimpleDateFormat("hh.mm aa dd/M/yyyy")

    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, RoomDB::class.java).build()

        dao = db.todoDao()

    }

    @After
    fun closeDB() {
        db.close()
    }

    @Test
    fun insertTodoItem() = runBlocking {
        val todoModel = TodoModel(null, "Medhat", "test description", Date())
        dao.insertTodoItem(todoModel)

        val todoItems = dao.getAllTodoList()

        assertThat(todoItems?.let {
            it[0].title == todoModel.title &&
                    it[0].description == todoModel.description &&
                    dateFormat.format(it[0].time) == dateFormat.format(todoModel.time)
        }).isTrue()
    }

    @Test
    fun deleteTodoItem() = runBlocking {
        val todoModel = TodoModel(null, "Medhat", "test description", Date())
        dao.insertTodoItem(todoModel)

        dao.deleteTodoItem(dao.getAllTodoList()?.get(0))

        val todoItems = dao.getAllTodoList()

        assertThat(todoItems?.isEmpty()).isTrue()
    }

    @Test
    fun update_TodoItem_New_Record_Found() = runBlocking {
        val todoModel = TodoModel(null, "Medhat", "test description", Date())
        dao.insertTodoItem(todoModel)

        val savedTodoItem = dao.getAllTodoList()?.get(0)
        savedTodoItem?.run {
            title = "Ahmed Medhat"
            description = "new description"
            time = Date()
        }

        dao.updateTodoItem(savedTodoItem?.id,
                savedTodoItem?.title ?: "",
                savedTodoItem?.description ?: "",
                savedTodoItem?.time ?: Date()
        )

        val todoItems = dao.getAllTodoList()

        assertThat(todoItems?.let {
            it[0].title == savedTodoItem?.title &&
                    it[0].description == savedTodoItem.description &&
                    dateFormat.format(it[0].time) == dateFormat.format(savedTodoItem.time)
        }).isTrue()
    }


    @Test
    fun update_TodoItem_Old_Record_Not_Found() = runBlocking {
        val todoModel = TodoModel(null, "Medhat", "test description", Date())
        dao.insertTodoItem(todoModel)

        val savedTodoItem = dao.getAllTodoList()?.get(0)
        savedTodoItem?.run {
            title = "Ahmed Medhat"
            description = "new description"
            time = Date()
        }

        dao.updateTodoItem(savedTodoItem?.id,
                savedTodoItem?.title ?: "",
                savedTodoItem?.description ?: "",
                savedTodoItem?.time ?: Date()
        )

        val todoItems = dao.getAllTodoList()

        assertThat(todoItems?.let {
            it[0].title == todoModel.title &&
                    it[0].description == todoModel.description &&
                    dateFormat.format(it[0].time) == dateFormat.format(todoModel.time)
        }).isFalse()
    }
}