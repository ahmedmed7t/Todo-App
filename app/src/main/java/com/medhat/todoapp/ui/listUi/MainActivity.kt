package com.medhat.todoapp.ui.listUi

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.medhat.todoapp.R
import com.medhat.todoapp.Services.NotificationBroadCastReceiver
import com.medhat.todoapp.data.model.TodoModel
import com.medhat.todoapp.ui.addItemUi.AddTodoItemActivity
import com.medhat.todoapp.ui.listUi.adpter.SwipeToDelete
import com.medhat.todoapp.ui.listUi.adpter.TodoRecyclerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    companion object {
        val TODO_ID = "TODO_ID"
        val TODO_TITLE = "TODO_TITLE"
        val TODO_DESCRIPTION = "TODO_DESCRIPTION"
        val TODO_TIME = "TODO_TIME"
        val CHANNEL_ID = "TODO_CHANNEL_ID"
    }

    lateinit var recyclerView: RecyclerView
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var todoRecyclerAdapter: TodoRecyclerAdapter
    lateinit var noDataFoundContainer: ConstraintLayout
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        mainViewModel.createNotificationChannel(this)

        listenToViewModelValues()

        floatingActionButton.setOnClickListener {
            gotoAddScreen(null)
        }
    }

    override fun onResume() {
        super.onResume()
        showLoading()
        mainViewModel.getAllTodoList()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.Todo_List_Recycler)
        floatingActionButton = findViewById(R.id.Todo_List_Add_Item_Floating)
        noDataFoundContainer = findViewById(R.id.Todo_List_No_Item_Found_Container)
        progressBar = findViewById(R.id.To_Do_List_Loading_Progress)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        todoRecyclerAdapter = TodoRecyclerAdapter(ArrayList(), mainViewModel)
        recyclerView.adapter = todoRecyclerAdapter
        (recyclerView.adapter as TodoRecyclerAdapter).notifyDataSetChanged()
        val touchListener = ItemTouchHelper(SwipeToDelete(todoRecyclerAdapter))
        touchListener.attachToRecyclerView(recyclerView)
    }

    @SuppressLint("ShowToast")
    fun listenToViewModelValues() {
        mainViewModel.todoList.observe(this, androidx.lifecycle.Observer {
            hideLoading()
            todoRecyclerAdapter.refreshList(ArrayList())
            if (it.isNotEmpty()) {
                hideNoDataFound()
                todoRecyclerAdapter.refreshList(ArrayList(it))
            } else {
                showNoDataFound()
            }
        })

        mainViewModel.toastMessage.observe(this, androidx.lifecycle.Observer {
                Toast.makeText(this, getString(it), Toast.LENGTH_LONG).show()
        })

        mainViewModel.gotoDetails.observe(this, androidx.lifecycle.Observer {
            gotoAddScreen(it)
        })

        mainViewModel.cancelAlarm.observe(this, Observer {
            mainViewModel.cancelAlarm(it,this)
        })
    }

    private fun showLoading() = runOnUiThread { progressBar.visibility = View.VISIBLE }

    private fun hideLoading() = runOnUiThread { progressBar.visibility = View.GONE }

    private fun gotoAddScreen(item: TodoModel?) {
        val intent = Intent(this, AddTodoItemActivity::class.java)
        item?.let {
            intent.putExtra(TODO_ID, it.id)
            intent.putExtra(TODO_TITLE, it.title)
            intent.putExtra(TODO_DESCRIPTION, it.description)
            intent.putExtra(TODO_TIME, it.time)
        }
        startActivity(intent)
    }

    private fun showNoDataFound() {
        noDataFoundContainer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideNoDataFound() {
        noDataFoundContainer.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}