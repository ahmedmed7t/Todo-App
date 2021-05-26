package com.medhat.todoapp.ui.listUi


import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medhat.todoapp.R
import com.medhat.todoapp.Services.NotificationBroadCastReceiver
import com.medhat.todoapp.data.model.ListCaller
import com.medhat.todoapp.data.model.TodoModel
import com.medhat.todoapp.data.repos.TodoRepo
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainViewModel(val todoRepo: TodoRepo) : ViewModel(), ListCaller {
    private val _todoList: MutableLiveData<ArrayList<TodoModel>> = MutableLiveData()
    val todoList: LiveData<ArrayList<TodoModel>>
        get() = _todoList

    private val _toastMessage: MutableLiveData<Int> = MutableLiveData()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    private val _gotoDetails: MutableLiveData<TodoModel> = MutableLiveData()
    val gotoDetails: LiveData<TodoModel>
        get() = _gotoDetails

    private val _cancelAlarm: MutableLiveData<Int> = MutableLiveData()
    val cancelAlarm: LiveData<Int>
        get() = _cancelAlarm

    fun getAllTodoList() {
        viewModelScope.launch {
            todoRepo.getAllTodoItems().let {
                _todoList.postValue(ArrayList(it))
            }
        }
    }

    override fun onItemDeleted(item: TodoModel) {
        viewModelScope.launch {
            todoRepo.deleteTodoItem(item).let {
                _toastMessage.postValue(R.string.item_deleted)
                _todoList.value?.remove(item)
                _todoList.postValue(_todoList.value)
                _cancelAlarm.postValue(item.time.time.toInt())
            }
        }

    }

    override fun onItemClicked(item: TodoModel) {
        _gotoDetails.postValue(item)
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MainActivity.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun cancelAlarm(id: Int, context: Context) {
        val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(context.applicationContext, NotificationBroadCastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
                context.applicationContext, id, myIntent, 0
        )

        alarmManager.cancel(pendingIntent)
    }

}