package com.medhat.todoapp.ui.listUi.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.medhat.todoapp.Utils.DateUtils
import com.medhat.todoapp.R
import com.medhat.todoapp.data.model.ListCaller
import com.medhat.todoapp.data.model.TodoModel

class TodoRecyclerAdapter(private var todoList: ArrayList<TodoModel>, val listCaller: ListCaller) : RecyclerView.Adapter<TodoRecyclerAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoTitle: TextView = itemView.findViewById(R.id.Todo_List_Item_Title_TextView)
        val todoDescription: TextView = itemView.findViewById(R.id.Todo_List_Item_Description_TextView)
        val todoDateTime: TextView = itemView.findViewById(R.id.Todo_List_Item_Date_Time_TextView)
        val lineDivider: View = itemView.findViewById(R.id.Todo_List_Item_Divider_Line_View)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.todoTitle.text = todoList[position].title
        holder.todoDescription.text = todoList[position].description
        holder.todoDateTime.text = DateUtils.getFormattedDate(todoList[position].time)
        if (position == todoList.size - 1)
            holder.lineDivider.visibility = View.GONE
        else
            holder.lineDivider.visibility = View.VISIBLE

        holder.itemView.setOnClickListener {
            listCaller.onItemClicked(todoList[position])
        }
    }

    fun deleteItem(position: Int) {
        listCaller.onItemDeleted(todoList[position])
        todoList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun refreshList(list: ArrayList<TodoModel>) {
        todoList = list
        notifyDataSetChanged()
    }
}