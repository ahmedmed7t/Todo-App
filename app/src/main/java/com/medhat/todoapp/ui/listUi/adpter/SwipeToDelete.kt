package com.medhat.todoapp.ui.listUi.adpter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDelete(val todoRecyclerAdapter: TodoRecyclerAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        todoRecyclerAdapter.deleteItem(viewHolder.adapterPosition)
    }
}