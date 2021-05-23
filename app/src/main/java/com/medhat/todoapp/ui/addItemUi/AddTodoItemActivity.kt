package com.medhat.todoapp.ui.addItemUi

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.medhat.todoapp.R
import com.medhat.todoapp.Services.NotificationBroadCastReceiver
import com.medhat.todoapp.Utils.DateUtils
import com.medhat.todoapp.data.model.TodoModel
import com.medhat.todoapp.ui.listUi.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AddTodoItemActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    lateinit var titleEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var timeEditText: EditText

    lateinit var titleError: TextView
    lateinit var descriptionError: TextView
    lateinit var timeError: TextView
    lateinit var timeIcon: ImageView

    lateinit var saveButton: TextView

    var isNewItem = true
    var selectedDay = 0
    var selectedMonth = 0
    var selectedYear = 0

    var todoDate: Date? = null

    private val addViewModel: AddTodoItemViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo_item)
        initViews()

        val myIntent = intent
        if (myIntent.hasExtra(MainActivity.TODO_ID)) {
            isNewItem = false
            showOldDate(getTodoItemModelFromIntent(myIntent))
        } else {
            isNewItem = true
        }

        trackTodoTitle()
        trackTodoDescription()
        trackTodoTime()

        handleSaveButtonClick()

        listenToViewModelModules()

        timeIcon.setOnClickListener {
            opeDateTimeDialog()
        }

        timeEditText.setOnClickListener {
            opeDateTimeDialog()
        }
    }

    private fun initViews() {
        titleEditText = findViewById(R.id.Add_New_Todo_Title_EditText)
        descriptionEditText = findViewById(R.id.Add_New_Todo_Description_EditText)
        timeEditText = findViewById(R.id.Add_New_Todo_Time_EditText)

        titleError = findViewById(R.id.Add_New_Todo_Title_Error_TextView)
        descriptionError = findViewById(R.id.Add_New_Todo_Description_Error_TextView)
        timeError = findViewById(R.id.Add_New_Todo_Time_Error_TextView)
        timeIcon = findViewById(R.id.Add_New_Todo_Time_Icon_Click)

        saveButton = findViewById(R.id.Add_New_Todo_Save_TextView)
    }

    private fun listenToViewModelModules() {
        addViewModel.isProcessFinished.observe(this, androidx.lifecycle.Observer {
            if (it) {
                Toast.makeText(this, getString(R.string.item_saved), Toast.LENGTH_LONG).show()
                onBackPressed()
            }
        })
    }

    private fun showOldDate(item: TodoModel) {
        titleEditText.setText(item.title)
        descriptionEditText.setText(item.description)
        timeEditText.setText(DateUtils.getFormattedDate(item.time))
    }

    private fun getTodoItemModelFromIntent(myIntent: Intent): TodoModel =
            TodoModel(
                    myIntent.getIntExtra(MainActivity.TODO_ID, 0),
                    myIntent.getStringExtra(MainActivity.TODO_TITLE) ?: "",
                    myIntent.getStringExtra(MainActivity.TODO_DESCRIPTION) ?: "",
                    DateUtils.getDateFromString(myIntent.getStringExtra(MainActivity.TODO_TIME)
                            ?: "")
                            ?: Date()
            )

    private fun opeDateTimeDialog() {
        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
                DatePickerDialog(this, this, year, month, day)
        datePickerDialog.show()
    }

    private fun handleSaveButtonClick() {
        saveButton.setOnClickListener {
            if (titleEditText.text.toString().isNotEmpty() &&
                    descriptionEditText.text.toString().isNotEmpty() &&
                    timeEditText.text.toString().isNotEmpty()
            ) {
                val todoModel = collectTodoModel()
                addViewModel.addTodoItem(todoModel, isNewItem)

                if (!isNewItem) {
                    val oldTime = DateUtils.getDateFromString(intent.getStringExtra(MainActivity.TODO_TIME)
                            ?: "")
                            ?: Date()
                    cancelAlarm(oldTime.time.toInt())
                }

                val time: Long = todoModel.time.time - (Date().time + (10 * 60 * 1000))
                val differenceBetweenTwoTimes = todoModel.time.time - (Date().time)
                if (differenceBetweenTwoTimes >= (10 * 60 * 1000))
                    startAlert(time, todoModel.time.time.toInt(), todoModel.title, todoModel.description)
            } else {
                showErrors()
            }
        }
    }

    private fun collectTodoModel(): TodoModel {
        val model = TodoModel(
                null,
                titleEditText.text.toString(),
                descriptionEditText.text.toString(),
                todoDate ?: Date()
        )
        if (!isNewItem)
            model.id = intent.getIntExtra(MainActivity.TODO_ID, 0)
        return model
    }


    private fun trackTodoTitle() {
        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty())
                    titleError.visibility = View.GONE
            }
        })
    }

    private fun trackTodoDescription() {
        descriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty())
                    descriptionError.visibility = View.GONE
            }
        })
    }

    private fun trackTodoTime() {
        timeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty())
                    timeError.visibility = View.GONE
            }
        })
    }

    private fun showErrors() {
        if (titleEditText.text.toString().isEmpty()) {
            titleError.visibility = View.VISIBLE
        } else {
            titleError.visibility = View.GONE
        }

        if (descriptionEditText.text.toString().isEmpty()) {
            descriptionError.visibility = View.VISIBLE
        } else {
            descriptionError.visibility = View.GONE
        }

        if (timeEditText.text.toString().isEmpty()) {
            timeError.visibility = View.VISIBLE
        } else {
            timeError.visibility = View.GONE
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        selectedDay = dayOfMonth
        selectedYear = year
        selectedMonth = month + 1
        val calendar: Calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, hour, minute,
                false)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        var time = (hourOfDay % 12).toString() + "." + if (minute >= 10) minute else "0${minute} "
        time += " " + if (hourOfDay >= 12) "PM" else "AM"
        val dateTime = time + " " + selectedDay + "/" + selectedMonth + "/" + selectedYear
        todoDate = DateUtils.getDateFromString(dateTime)
        timeEditText.setText(dateTime)
    }

    private fun startAlert(time: Long, id: Int, title: String, content: String) {
        val intent = Intent(this, NotificationBroadCastReceiver::class.java)
        intent.putExtra("Title", title)
        intent.putExtra("Content", content)
        val pendingIntent = PendingIntent.getBroadcast(
                this.applicationContext, id, intent, 0
        )
        val alarmManager =
                getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + time] = pendingIntent
    }

    private fun cancelAlarm(id: Int) {
        val alarmManager =
                getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(applicationContext, NotificationBroadCastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
                applicationContext, id, myIntent, 0
        )

        alarmManager.cancel(pendingIntent)
    }
}