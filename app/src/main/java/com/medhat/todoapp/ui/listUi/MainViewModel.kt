package com.medhat.todoapp.ui.listUi

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
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

class MainViewModel(val todoRepo: TodoRepo, val context: Context) : ViewModel(), ListCaller {
    private val _todoList: MutableLiveData<ArrayList<TodoModel>> = MutableLiveData()
    val todoList: LiveData<ArrayList<TodoModel>>
        get() = _todoList

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String>
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
                _toastMessage.postValue(context.getString(R.string.item_deleted))
                _todoList.value?.remove(item)
                _todoList.postValue(_todoList.value)
                _cancelAlarm.postValue(item.time.time.toInt())
            }
        }

    }

    override fun onItemClicked(item: TodoModel) {
        _gotoDetails.postValue(item)
    }

}