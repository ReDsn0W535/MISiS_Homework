package com.example.misis.TodoItemRepository

import androidx.lifecycle.ViewModel
import com.example.misis.TodoItem.TodoItem


class TodoItemRepository : ViewModel() {
    fun makeNewTask(task: TodoItem) {
        tasks.add(task)
    }

    fun getTasks(): List<TodoItem> {
        return tasks
    }

    fun getTopTask(): TodoItem {
        return tasks[0]
    }

    fun setTask(task: TodoItem) {
        tasks.add(task);
        TaskStorage.writeItem(task)
    }

    private var tasks: MutableList<TodoItem> = mutableStateListOf();
}