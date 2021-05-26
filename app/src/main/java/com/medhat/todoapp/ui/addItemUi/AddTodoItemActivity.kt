package com.medhat.todoapp.ui.addItemUi

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.medhat.todoapp.R
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

        if (intent.hasExtra(MainActivity.TODO_ID)) {
            isNewItem = false
            showOldDate(addViewModel.getTodoItemModelFromIntent(intent))
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

            if (!isNewItem) {
                val oldTime = DateUtils.getDateFromString(intent.getStringExtra(MainActivity.TODO_TIME)
                        ?: "")
                        ?: Date()
                addViewModel.cancelAlarm(oldTime.time.toInt(),this)
            }

            val time: Long = it.time.time - (Date().time + (10 * 60 * 1000))
            val differenceBetweenTwoTimes = it.time.time - (Date().time)
            if (differenceBetweenTwoTimes >= (10 * 60 * 1000))
                addViewModel.startAlert(time, it.time.time.toInt(), it.title,
                        it.description, this)

            Toast.makeText(this, getString(R.string.item_saved), Toast.LENGTH_LONG).show()
            onBackPressed()
        })

        addViewModel.validationError.observe(this, androidx.lifecycle.Observer {
            if(it){
                showErrors()
            }
        })
    }

    private fun showOldDate(item: TodoModel) {
        titleEditText.setText(item.title)
        descriptionEditText.setText(item.description)
        timeEditText.setText(DateUtils.getFormattedDate(item.time))
    }

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
            var oldId  : Int? = null
            if(!isNewItem)
               oldId = intent.getIntExtra(MainActivity.TODO_ID, 0)

           addViewModel.canAddTodOItem(titleEditText.text.toString(), titleEditText.text.toString(),
                    timeEditText.text.toString(), oldId, isNewItem )
        }
    }

    private fun trackTodoTitle() {
        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty())
                    titleError.visibility = View.GONE
            }
        })
    }

    private fun trackTodoDescription() {
        descriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty())
                    descriptionError.visibility = View.GONE
            }
        })
    }

    private fun trackTodoTime() {
        timeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

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

}