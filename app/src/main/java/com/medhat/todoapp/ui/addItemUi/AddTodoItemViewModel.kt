package com.medhat.todoapp.ui.addItemUi


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medhat.todoapp.Services.NotificationBroadCastReceiver
import com.medhat.todoapp.Utils.DateUtils
import com.medhat.todoapp.data.model.TodoModel
import com.medhat.todoapp.data.repos.AddTodoItemRepo
import com.medhat.todoapp.ui.listUi.MainActivity
import kotlinx.coroutines.launch
import java.util.*

class AddTodoItemViewModel(private val addItemRepo: AddTodoItemRepo) : ViewModel() {

    private val _isProcessFinished = MutableLiveData<TodoModel>()
    val isProcessFinished: LiveData<TodoModel>
        get() = _isProcessFinished

    private val _validationError = MutableLiveData<Boolean>()
    val validationError: LiveData<Boolean>
        get() = _validationError

    fun addTodoItem(item: TodoModel, isNewItem: Boolean) {

        viewModelScope.launch {
            if (isNewItem) {
                addItemRepo.insertTodoItem(item).let {
                    _isProcessFinished.postValue(item)
                }

            } else {
                addItemRepo.updateTodoItem(item).let {
                    _isProcessFinished.postValue(item)
                }
            }
        }
    }

    fun canAddTodOItem(title : String, description : String , date : String, id : Int?, isNewItem: Boolean) {
        if( title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()){
            val model = TodoModel(
                    id,
                    title,
                    description,
                    DateUtils.getDateFromString(date) ?: Date()
            )
            addTodoItem(model, isNewItem)
            _validationError.postValue(false)
        }else{
            _validationError.postValue(true)
        }
    }


    fun getTodoItemModelFromIntent(myIntent: Intent): TodoModel =
            TodoModel(
                    myIntent.getIntExtra(MainActivity.TODO_ID, 0),
                    myIntent.getStringExtra(MainActivity.TODO_TITLE) ?: "",
                    myIntent.getStringExtra(MainActivity.TODO_DESCRIPTION) ?: "",
                    DateUtils.getDateFromString(myIntent.getStringExtra(MainActivity.TODO_TIME)
                            ?: "")
                            ?: Date()
            )

    fun startAlert(time: Long, id: Int, title: String, content: String, context: Context) {
        val intent = Intent(context, NotificationBroadCastReceiver::class.java)
        intent.putExtra("Title", title)
        intent.putExtra("Content", content)
        val pendingIntent = PendingIntent.getBroadcast(
                context.applicationContext, id, intent, 0
        )
        val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + time] = pendingIntent
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