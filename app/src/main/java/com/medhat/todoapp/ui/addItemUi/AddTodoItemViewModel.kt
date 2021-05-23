package com.medhat.todoapp.ui.addItemUi

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medhat.todoapp.Services.NotificationBroadCastReceiver
import com.medhat.todoapp.data.model.TodoModel
import com.medhat.todoapp.data.repos.AddTodoItemRepo
import kotlinx.coroutines.launch

class AddTodoItemViewModel(val addItemRepo: AddTodoItemRepo) : ViewModel() {

    private val _isProcessFinished = MutableLiveData<Boolean>()
    val isProcessFinished: LiveData<Boolean>
        get() = _isProcessFinished


    fun addTodoItem(item: TodoModel, isNewItem: Boolean) {
        viewModelScope.launch {
            if (isNewItem) {
                addItemRepo.insertTodoItem(item).let {
                    _isProcessFinished.postValue(true)
                }

            } else {
                addItemRepo.updateTodoItem(item).let {
                    _isProcessFinished.postValue(true)
                }
            }
        }
    }

}